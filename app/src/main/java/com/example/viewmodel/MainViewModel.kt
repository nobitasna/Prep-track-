package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.defaultdata.DefaultSubjectTemplate
import com.example.data.defaultdata.DefaultSyllabusCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.ExamGoalEntity
import com.example.data.local.entity.FocusAllowedAppEntity
import com.example.data.local.entity.FocusSessionEntity
import com.example.data.local.entity.FocusSettingsEntity
import com.example.data.local.entity.LectureEntity
import com.example.data.local.entity.StudyPlanEntity
import com.example.data.local.entity.SubjectEntity
import com.example.data.local.entity.UserStreakEntity
import com.example.data.local.entity.WeeklyTimetableEntryEntity
import com.example.data.model.ActivationResult
import com.example.data.model.DeviceAppInfo
import com.example.data.model.StudyPlanCalculation
import com.example.data.model.SubscriptionInfo
import com.example.data.model.SubscriptionTier
import com.example.data.repository.PrepTrackRepository
import com.example.data.repository.SubscriptionRepository
import com.example.enforcement.FocusEnforcementManager
import com.example.permission.FocusPermissionManager
import com.example.util.InstalledAppsManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

sealed class AppScreen {
    object Splash : AppScreen()
    object Onboarding : AppScreen()
    object Login : AppScreen()
    object SubscriptionSelection : AppScreen()
    object OnboardingWelcome : AppScreen()
    object OnboardingSelectGoal : AppScreen()
    object Dashboard : AppScreen()
    object Plan : AppScreen()
    object Syllabus : AppScreen()
    data class ChapterDetail(val chapterId: Long) : AppScreen()
    object Focus : AppScreen()
    object FocusPermission : AppScreen()
    object ChooseAllowedApps : AppScreen()
    object Analytics : AppScreen()
    object Settings : AppScreen()
}

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val uid: String? = null,
    val isLoggedIn: Boolean = false
)

data class SubjectProgressData(
    val subject: SubjectEntity,
    val totalLectures: Int,
    val completedLectures: Int,
    val referenceHours: Double,
    val completedReferenceHours: Double,
    val estimatedWatchHours: Double,
    val percent: Int
)

data class TodayTaskItem(
    val id: Long,
    val lectureId: Long,
    val subjectName: String,
    val subjectColor: String,
    val chapterName: String,
    val lectureTitle: String,
    val lectureNumber: Int,
    val isCompleted: Boolean,
    val notesCompleted: Boolean,
    val practiceCompleted: Boolean
)

data class TodayProgressSummary(
    val totalTasks: Int,
    val completedLectures: Int,
    val notesDone: Int,
    val practiceDone: Int,
    val progressFraction: Float,
    val completedWorkloadHours: Float,
    val completedWorkloadFormatted: String,
    val remainingWorkloadFormatted: String,
    val targetWorkloadHours: Float,
    val targetWorkloadFormatted: String,
    val isGoalAchieved: Boolean
)

data class PomodoroUiState(
    val isRunning: Boolean = false,
    val isPaused: Boolean = false,
    val isBreak: Boolean = false,
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val selectedSubject: String = "Physics",
    val selectedChapter: String = "",
    val selectedTopic: String = "",
    val currentTaskName: String = "",
    val completedFocusMinutesToday: Int = 0,
    val selectedDistractionShield: Boolean = true,
    val strictModeEnabled: Boolean = false,
    val ambientSoundType: String = "NONE",
    val activeSessionId: Long? = null,
    val allowedAppsCount: Int = 0,
    val sessionStartTime: Long = 0L,
    val currentSegmentStartTime: Long = 0L,
    val accumulatedActiveDurationMs: Long = 0L,
    val accumulatedPausedDurationMs: Long = 0L,
    val lastPauseTimestamp: Long = 0L,
    val elapsedSecondsTicker: Long = 0L
) {
    fun getActiveDurationSeconds(): Long {
        val activeMs = if (isRunning && !isPaused && currentSegmentStartTime > 0L) {
            accumulatedActiveDurationMs + (System.currentTimeMillis() - currentSegmentStartTime).coerceAtLeast(0L)
        } else {
            accumulatedActiveDurationMs
        }
        return (activeMs / 1000L).coerceAtLeast(0L)
    }

    fun getPausedDurationSeconds(): Long {
        val pausedMs = if (isPaused && lastPauseTimestamp > 0L) {
            accumulatedPausedDurationMs + (System.currentTimeMillis() - lastPauseTimestamp).coerceAtLeast(0L)
        } else {
            accumulatedPausedDurationMs
        }
        return (pausedMs / 1000L).coerceAtLeast(0L)
    }

    fun getFormattedElapsedTime(): String {
        val totalSecs = getActiveDurationSeconds()
        val hours = totalSecs / 3600
        val minutes = (totalSecs % 3600) / 60
        val seconds = totalSecs % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}

class MainViewModel(private val app: Application) : AndroidViewModel(app) {

    private val repository: PrepTrackRepository
    private val authPrefs = app.getSharedPreferences("prep_track_auth", android.content.Context.MODE_PRIVATE)
    private val subscriptionRepository = SubscriptionRepository(app)
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseCurrentUser = firebaseAuth.currentUser

    private val isInitiallyLoggedIn = firebaseCurrentUser != null || authPrefs.getBoolean("is_logged_in", false)

    private val _userProfile = MutableStateFlow(
        if (firebaseCurrentUser != null) {
            UserProfile(
                name = firebaseCurrentUser.displayName ?: firebaseCurrentUser.email?.substringBefore("@") ?: "Student",
                email = firebaseCurrentUser.email ?: "",
                photoUrl = firebaseCurrentUser.photoUrl?.toString(),
                uid = firebaseCurrentUser.uid,
                isLoggedIn = true
            )
        } else if (authPrefs.getBoolean("is_logged_in", false)) {
            UserProfile(
                name = authPrefs.getString("user_name", "") ?: "",
                email = authPrefs.getString("user_email", "") ?: "",
                photoUrl = authPrefs.getString("user_photo", null),
                uid = authPrefs.getString("user_uid", null),
                isLoggedIn = true
            )
        } else {
            UserProfile(
                name = "",
                email = "",
                photoUrl = null,
                uid = null,
                isLoggedIn = false
            )
        }
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _authError = MutableStateFlow<String?>(null)
    val authError: StateFlow<String?> = _authError.asStateFlow()

    private val _isAuthLoading = MutableStateFlow(false)
    val isAuthLoading: StateFlow<Boolean> = _isAuthLoading.asStateFlow()

    fun clearAuthError() {
        _authError.value = null
    }

    fun setAuthError(message: String) {
        _authError.value = message
    }

    private val _subscriptionInfo = MutableStateFlow(
        subscriptionRepository.getSubscription(_userProfile.value.email)
    )
    val subscriptionInfo: StateFlow<SubscriptionInfo> = _subscriptionInfo.asStateFlow()

    private val _currentScreen = MutableStateFlow<AppScreen>(
        if (!isInitiallyLoggedIn) {
            AppScreen.Splash
        } else {
            val initialSub = subscriptionRepository.getSubscription(_userProfile.value.email)
            if (initialSub.tier == SubscriptionTier.NONE) {
                AppScreen.SubscriptionSelection
            } else {
                AppScreen.Dashboard
            }
        }
    )
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<AppScreen>()

    fun navigateTo(screen: AppScreen) {
        if (_currentScreen.value != screen) {
            if (_screenHistory.lastOrNull() != _currentScreen.value) {
                _screenHistory.add(_currentScreen.value)
            }
            if (_screenHistory.size > 25) {
                _screenHistory.removeAt(0)
            }
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.size - 1)
            return true
        }
        return false
    }

    /**
     * Authenticates the Google ID token obtained from Credential Manager with Firebase Authentication.
     * Uses real Firebase user profile (UID, email, displayName, photoUrl).
     */
    fun signInWithFirebaseGoogleToken(idToken: String, onComplete: ((Boolean, String?) -> Unit)? = null) {
        _isAuthLoading.value = true
        _authError.value = null
        viewModelScope.launch {
            try {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = suspendCancellableCoroutine { continuation ->
                    firebaseAuth.signInWithCredential(credential)
                        .addOnSuccessListener { result ->
                            if (continuation.isActive) continuation.resume(result)
                        }
                        .addOnFailureListener { exception ->
                            if (continuation.isActive) continuation.resumeWithException(exception)
                        }
                }

                val user = authResult.user
                if (user != null) {
                    val realName = user.displayName ?: user.email?.substringBefore("@") ?: "Student"
                    val realEmail = user.email ?: "user@preptrack.com"
                    val realPhoto = user.photoUrl?.toString()
                    val realUid = user.uid

                    authPrefs.edit()
                        .putBoolean("is_logged_in", true)
                        .putString("user_name", realName)
                        .putString("user_email", realEmail)
                        .putString("user_photo", realPhoto)
                        .putString("user_uid", realUid)
                        .apply()

                    _userProfile.value = UserProfile(
                        name = realName,
                        email = realEmail,
                        photoUrl = realPhoto,
                        uid = realUid,
                        isLoggedIn = true
                    )

                    val sub = subscriptionRepository.getSubscription(realEmail)
                    _subscriptionInfo.value = sub

                    if (sub.tier == SubscriptionTier.NONE) {
                        _currentScreen.value = AppScreen.SubscriptionSelection
                    } else {
                        val goal = activeGoal.value
                        if (goal != null) {
                            _currentScreen.value = AppScreen.Dashboard
                        } else {
                            _currentScreen.value = AppScreen.OnboardingSelectGoal
                        }
                    }
                    onComplete?.invoke(true, null)
                } else {
                    val msg = "Firebase Authentication could not retrieve user."
                    _authError.value = msg
                    onComplete?.invoke(false, msg)
                }
            } catch (e: Exception) {
                val msg = e.localizedMessage ?: "Firebase Authentication failed."
                _authError.value = msg
                onComplete?.invoke(false, msg)
            } finally {
                _isAuthLoading.value = false
            }
        }
    }

    fun signInWithGoogle(name: String = "Shubh Anand", email: String = "nobitanobi7209@gmail.com") {
        authPrefs.edit()
            .putBoolean("is_logged_in", true)
            .putString("user_name", name)
            .putString("user_email", email)
            .apply()
        _userProfile.value = UserProfile(name = name, email = email, isLoggedIn = true)

        val sub = subscriptionRepository.getSubscription(email)
        _subscriptionInfo.value = sub

        if (sub.tier == SubscriptionTier.NONE) {
            _currentScreen.value = AppScreen.SubscriptionSelection
        } else {
            val goal = activeGoal.value
            if (goal != null) {
                _currentScreen.value = AppScreen.Dashboard
            } else {
                _currentScreen.value = AppScreen.OnboardingSelectGoal
            }
        }
    }

    fun loginWithEmail(email: String, password: String) {
        val derivedName = if (email.contains("@")) {
            email.substringBefore("@").replace(".", " ")
                .split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
        } else "Shubh Anand"
        signInWithGoogle(name = derivedName.ifEmpty { "Shubh Anand" }, email = email.ifEmpty { "nobitanobi7209@gmail.com" })
    }

    fun start3DayTrial() {
        val updated = subscriptionRepository.start3DayTrial(_userProfile.value.email)
        _subscriptionInfo.value = updated
        val goal = activeGoal.value
        if (goal != null) {
            _currentScreen.value = AppScreen.Dashboard
        } else {
            _currentScreen.value = AppScreen.OnboardingSelectGoal
        }
    }

    fun activateProWithKey(enteredKey: String): Pair<Boolean, String> {
        val result = subscriptionRepository.activateProWithKey(_userProfile.value.email, enteredKey)
        return when (result) {
            is ActivationResult.Success -> {
                val updated = subscriptionRepository.getSubscription(_userProfile.value.email)
                _subscriptionInfo.value = updated
                if (_currentScreen.value is AppScreen.SubscriptionSelection) {
                    val goal = activeGoal.value
                    if (goal != null) {
                        _currentScreen.value = AppScreen.Dashboard
                    } else {
                        _currentScreen.value = AppScreen.OnboardingSelectGoal
                    }
                }
                Pair(true, "Pro activated successfully!")
            }
            is ActivationResult.Error -> {
                Pair(false, result.message)
            }
        }
    }

    fun expireTrialForTesting() {
        val updated = subscriptionRepository.expireTrialForTesting(_userProfile.value.email)
        _subscriptionInfo.value = updated
    }

    fun resetTrialForTesting() {
        val updated = subscriptionRepository.resetTrialForTesting(_userProfile.value.email)
        _subscriptionInfo.value = updated
        _currentScreen.value = AppScreen.SubscriptionSelection
    }

    fun refreshSubscription() {
        _subscriptionInfo.value = subscriptionRepository.getSubscription(_userProfile.value.email)
    }

    fun signOut() {
        try {
            firebaseAuth.signOut()
        } catch (_: Exception) {}
        authPrefs.edit()
            .putBoolean("is_logged_in", false)
            .remove("user_name")
            .remove("user_email")
            .remove("user_photo")
            .remove("user_uid")
            .apply()
        _userProfile.value = UserProfile(isLoggedIn = false)
        _currentScreen.value = AppScreen.Login
    }

    init {
        val database = AppDatabase.getInstance(app)
        repository = PrepTrackRepository(database)

        if (!isInitiallyLoggedIn) {
            _currentScreen.value = AppScreen.Splash
        }

        // Periodic ticker to auto-detect trial expiration in real time
        viewModelScope.launch {
            while (true) {
                delay(30_000)
                if (_userProfile.value.isLoggedIn) {
                    _subscriptionInfo.value = subscriptionRepository.getSubscription(_userProfile.value.email)
                }
            }
        }
    }

    // Database flows
    val activeGoal: StateFlow<ExamGoalEntity?> = repository.getActiveGoal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val allGoals: StateFlow<List<ExamGoalEntity>> = repository.getAllGoals()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val userStreak: StateFlow<UserStreakEntity?> = repository.getUserStreak()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Dynamic state based on active goal
    private val _subjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val subjects: StateFlow<List<SubjectEntity>> = _subjects.asStateFlow()

    private val _chapters = MutableStateFlow<List<ChapterEntity>>(emptyList())
    val chapters: StateFlow<List<ChapterEntity>> = _chapters.asStateFlow()

    private val _lectures = MutableStateFlow<List<LectureEntity>>(emptyList())
    val lectures: StateFlow<List<LectureEntity>> = _lectures.asStateFlow()

    private val _studyPlan = MutableStateFlow<StudyPlanEntity?>(null)
    val studyPlan: StateFlow<StudyPlanEntity?> = _studyPlan.asStateFlow()

    private val _timetable = MutableStateFlow<List<WeeklyTimetableEntryEntity>>(emptyList())
    val timetable: StateFlow<List<WeeklyTimetableEntryEntity>> = _timetable.asStateFlow()

    val allFocusSessions: StateFlow<List<FocusSessionEntity>> = repository.getAllFocusSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Calculations Flow
    val planCalculation: StateFlow<StudyPlanCalculation> = combine(
        _lectures,
        _studyPlan
    ) { allLectures, plan ->
        val activeLectures = allLectures.filter { !itChapterExcluded(it.chapterId) }
        val total = activeLectures.size
        val completed = activeLectures.count { it.isCompleted }
        val speed = plan?.playbackSpeed ?: 1.0f
        val notesMin = plan?.notesMinutesPerLecture ?: 30
        val practiceMin = plan?.practiceMinutesDaily ?: 60
        val revisionMin = plan?.revisionMinutesDaily ?: 30
        val targetDays = plan?.targetDays ?: 90
        val missedAdj = plan?.missedWorkloadHoursAdjustment ?: 0f

        StudyPlanCalculation.calculate(
            totalLectures = total,
            completedLectures = completed,
            playbackSpeed = speed,
            notesMinutesPerLecture = notesMin,
            dailyPracticeMinutes = practiceMin,
            dailyRevisionMinutes = revisionMin,
            targetDays = targetDays,
            missedAdjustmentHours = missedAdj
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        StudyPlanCalculation.calculate(0, 0, 1.0f, 30, 60, 30, 90)
    )

    // Subject Progress breakdown
    val subjectProgressList: StateFlow<List<SubjectProgressData>> = combine(
        _subjects,
        _chapters,
        _lectures,
        _studyPlan
    ) { subs, chaps, lecs, plan ->
        val speed = plan?.playbackSpeed ?: 1.0f
        subs.map { subject ->
            val subChapterIds = chaps.filter { it.subjectId == subject.id && !it.isHidden }.map { it.id }.toSet()
            val subLectures = lecs.filter { it.chapterId in subChapterIds }
            val total = subLectures.size
            val completed = subLectures.count { it.isCompleted }
            val refHours = total * 2.0
            val compRefHours = completed * 2.0
            val estWatch = refHours / speed
            val percent = if (total > 0) (completed * 100) / total else 0
            SubjectProgressData(
                subject = subject,
                totalLectures = total,
                completedLectures = completed,
                referenceHours = refHours,
                completedReferenceHours = compRefHours,
                estimatedWatchHours = estWatch,
                percent = percent
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Today's generated task plan
    val todayTasks: StateFlow<List<TodayTaskItem>> = combine(
        _subjects,
        _chapters,
        _lectures,
        _studyPlan,
        _timetable
    ) { subs, chaps, lecs, plan, timetableEntries ->
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val todayStart = cal.timeInMillis

        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) // Sun=1, Mon=2...
        val dayMapped = if (dayOfWeek == Calendar.SUNDAY) 7 else dayOfWeek - 1 // 1=Mon..7=Sun

        val assignedSubjectIds = if (plan?.studyPattern == "WEEKLY_TIMETABLE") {
            timetableEntries.filter { it.dayOfWeek == dayMapped }.map { it.subjectId }.toSet()
        } else {
            // One by one: pick the first subject that still has incomplete lectures
            val firstIncompleteSub = subs.firstOrNull { sub ->
                val chapIds = chaps.filter { it.subjectId == sub.id && !it.isHidden }.map { it.id }.toSet()
                lecs.any { it.chapterId in chapIds && !it.isCompleted }
            }
            if (firstIncompleteSub != null) setOf(firstIncompleteSub.id) else subs.take(1).map { it.id }.toSet()
        }

        val subjectMap = subs.associateBy { it.id }
        val chapterMap = chaps.associateBy { it.id }

        val tasks = mutableListOf<TodayTaskItem>()

        // 1. Always keep lectures completed today so user sees completed status & ticks!
        val completedToday = lecs.filter { it.isCompleted && it.completedAtTimestamp >= todayStart }
        for (lec in completedToday) {
            val chap = chapterMap[lec.chapterId]
            val sub = chap?.let { subjectMap[it.subjectId] }
            if (chap != null && sub != null) {
                tasks.add(
                    TodayTaskItem(
                        id = lec.id,
                        lectureId = lec.id,
                        subjectName = sub.name,
                        subjectColor = sub.colorHex,
                        chapterName = chap.name,
                        lectureTitle = lec.title,
                        lectureNumber = lec.lectureNumber,
                        isCompleted = lec.isCompleted,
                        notesCompleted = lec.notesCompleted,
                        practiceCompleted = lec.practiceCompleted
                    )
                )
            }
        }

        // Dynamically compute the exact daily task count needed to complete the syllabus in targetDays!
        val speed = plan?.playbackSpeed ?: 1.0f
        val notesMin = plan?.notesMinutesPerLecture ?: 30
        val targetDays = (plan?.targetDays ?: 90).coerceAtLeast(1)

        val totalActiveLecs = lecs.count { !itChapterExcluded(it.chapterId) }
        val requiredLecturesDaily = Math.ceil(totalActiveLecs.toDouble() / targetDays).toInt().coerceAtLeast(1)
        val targetDailyTaskCount = requiredLecturesDaily.coerceIn(1, 15)

        val filteredChaps = chaps.filter { (assignedSubjectIds.isEmpty() || it.subjectId in assignedSubjectIds) && !it.isHidden }

        // 2. Add pending incomplete lectures from assigned subjects
        for (chap in filteredChaps) {
            if (tasks.size >= targetDailyTaskCount) break
            val pendingLecs = lecs.filter { it.chapterId == chap.id && !it.isCompleted && tasks.none { t -> t.lectureId == it.id } }
            for (lec in pendingLecs) {
                val sub = subjectMap[chap.subjectId]
                tasks.add(
                    TodayTaskItem(
                        id = lec.id,
                        lectureId = lec.id,
                        subjectName = sub?.name ?: "Subject",
                        subjectColor = sub?.colorHex ?: "#1A73E8",
                        chapterName = chap.name,
                        lectureTitle = lec.title,
                        lectureNumber = lec.lectureNumber,
                        isCompleted = lec.isCompleted,
                        notesCompleted = lec.notesCompleted,
                        practiceCompleted = lec.practiceCompleted
                    )
                )
                if (tasks.size >= targetDailyTaskCount) break
            }
        }

        // 3. Fallback: if assigned subjects are empty or completed, add from any pending syllabus chapter
        if (tasks.size < targetDailyTaskCount) {
            val remainingSyllabus = lecs.filter { !it.isCompleted && tasks.none { t -> t.lectureId == it.id } }
                .take(targetDailyTaskCount - tasks.size)
            for (lec in remainingSyllabus) {
                val chap = chapterMap[lec.chapterId]
                val sub = chap?.let { subjectMap[it.subjectId] }
                if (chap != null && sub != null) {
                    tasks.add(
                        TodayTaskItem(
                            id = lec.id,
                            lectureId = lec.id,
                            subjectName = sub.name,
                            subjectColor = sub.colorHex,
                            chapterName = chap.name,
                            lectureTitle = lec.title,
                            lectureNumber = lec.lectureNumber,
                            isCompleted = lec.isCompleted,
                            notesCompleted = lec.notesCompleted,
                            practiceCompleted = lec.practiceCompleted
                        )
                    )
                }
            }
        }

        tasks
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Summary of today's progress, actions, and workload achievement
    val todayProgressSummary: StateFlow<TodayProgressSummary> = combine(
        todayTasks,
        planCalculation,
        _studyPlan
    ) { tasks, calc, plan ->
        val total = tasks.size
        val completedLec = tasks.count { it.isCompleted }
        val notesDone = tasks.count { it.notesCompleted }
        val practiceDone = tasks.count { it.practiceCompleted }

        // Dynamic task completion fraction:
        // Watch lecture = 60%, Notes = 20%, Practice DPP = 20%
        val fraction = if (total > 0) {
            val totalScore = tasks.sumOf { task ->
                (if (task.isCompleted) 0.6 else 0.0) +
                (if (task.notesCompleted) 0.2 else 0.0) +
                (if (task.practiceCompleted) 0.2 else 0.0)
            }
            (totalScore / total).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }

        val speed = plan?.playbackSpeed ?: 1.0f
        val notesMin = plan?.notesMinutesPerLecture ?: 30
        val practiceMin = plan?.practiceMinutesDaily ?: 60
        val revisionMin = plan?.revisionMinutesDaily ?: 30

        val lectureWatchHours = 2.0f / speed
        val lectureNotesHours = notesMin / 60.0f
        val dailyPracticeHours = practiceMin / 60.0f
        val dailyRevisionHours = revisionMin / 60.0f

        // The daily planned target hours is FIXED and matches calc.dailyEstimatedWorkloadHours
        val targetDailyHours = calc.dailyEstimatedWorkloadHours.toFloat().coerceAtLeast(0.1f)
        val targetFormatted = calc.dailyWorkloadFormatted

        // Workload completed: each lecture watched gives lectureWatchHours, each notes done gives lectureNotesHours,
        // and practice/revision are distributed across the planned daily tasks:
        val taskCountForDistribution = maxOf(total, calc.dailyLecturesRequired).coerceAtLeast(1)
        val completedWorkload = (completedLec * lectureWatchHours) +
                                (notesDone * lectureNotesHours) +
                                (practiceDone * ((dailyPracticeHours + dailyRevisionHours) / taskCountForDistribution))

        val remainingHours = (targetDailyHours - completedWorkload).coerceAtLeast(0f)
        val completedFormatted = StudyPlanCalculation.formatHoursMinutes(completedWorkload.toDouble())
        val remainingFormatted = StudyPlanCalculation.formatHoursMinutes(remainingHours.toDouble())

        val workloadFraction = (completedWorkload / targetDailyHours).coerceIn(0f, 1f)
        val taskFraction = if (total > 0) {
            val totalScore = tasks.sumOf { task ->
                (if (task.isCompleted) 0.6 else 0.0) +
                (if (task.notesCompleted) 0.2 else 0.0) +
                (if (task.practiceCompleted) 0.2 else 0.0)
            }
            (totalScore / total).toFloat().coerceIn(0f, 1f)
        } else 0f
        val finalFraction = maxOf(taskFraction, workloadFraction)
        val isAchieved = (completedWorkload >= targetDailyHours - 0.05f) || (total > 0 && completedLec == total && notesDone == total && practiceDone == total)

        TodayProgressSummary(
            totalTasks = total,
            completedLectures = completedLec,
            notesDone = notesDone,
            practiceDone = practiceDone,
            progressFraction = finalFraction,
            completedWorkloadHours = completedWorkload,
            completedWorkloadFormatted = completedFormatted,
            remainingWorkloadFormatted = remainingFormatted,
            targetWorkloadHours = targetDailyHours,
            targetWorkloadFormatted = targetFormatted,
            isGoalAchieved = isAchieved
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TodayProgressSummary(0, 0, 0, 0, 0f, 0f, "0h 0m", "0h 0m", 0f, "0h 0m", false)
    )

    // Focus Mode & Allowed Apps State
    val focusSettings: StateFlow<FocusSettingsEntity> = repository.getFocusSettings()
        .map { it ?: FocusSettingsEntity(id = 1) }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            FocusSettingsEntity(id = 1)
        )

    val allowedApps: StateFlow<List<FocusAllowedAppEntity>> = repository.getAllAllowedApps()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    val enabledAllowedApps: StateFlow<List<FocusAllowedAppEntity>> = repository.getEnabledAllowedApps()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            emptyList()
        )

    val activeFocusSession: StateFlow<FocusSessionEntity?> = repository.getActiveFocusSession()
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    // Focus Permission State (Phase 3)
    private val _focusPermissionState = MutableStateFlow(
        FocusPermissionManager.checkPermissionState(
            app,
            wasPreviouslyGranted = authPrefs.getBoolean("focus_permission_ever_granted", false),
            hasAttemptedGrant = authPrefs.getBoolean("focus_permission_attempted", false)
        )
    )
    val focusPermissionState: StateFlow<FocusPermissionManager.FocusPermissionState> = _focusPermissionState.asStateFlow()

    // Dynamically Discovered Device Apps (Phase 4)
    private val _installedDeviceApps = MutableStateFlow<List<DeviceAppInfo>>(emptyList())
    val installedDeviceApps: StateFlow<List<DeviceAppInfo>> = _installedDeviceApps.asStateFlow()

    private val _isInstalledAppsLoading = MutableStateFlow(false)
    val isInstalledAppsLoading: StateFlow<Boolean> = _isInstalledAppsLoading.asStateFlow()

    // Real Enforcement State (Phase 5)
    val enforcementState: StateFlow<FocusEnforcementManager.EnforcementState> = FocusEnforcementManager.enforcementState
    val lastBlockedPackage: StateFlow<String?> = FocusEnforcementManager.lastBlockedPackage
    val enforcementTier: StateFlow<FocusEnforcementManager.EnforcementTier> = FocusEnforcementManager.enforcementTier

    // Pomodoro Timer State
    private val _pomodoroState = MutableStateFlow(PomodoroUiState())
    val pomodoroState: StateFlow<PomodoroUiState> = _pomodoroState.asStateFlow()
    private var timerJob: Job? = null

    init {
        // Initial check for Focus Mode permissions
        refreshFocusPermissions(app)

        // Observe enforcement state to automatically react if permission is revoked in background
        viewModelScope.launch {
            FocusEnforcementManager.enforcementState.collect { enfState ->
                if (enfState == FocusEnforcementManager.EnforcementState.PERMISSION_REVOKED) {
                    if (_pomodoroState.value.isRunning) {
                        pausePomodoro()
                    }
                    refreshFocusPermissions(app)
                }
            }
        }

        // Initialize Focus Settings & Allowed Apps, restore state across recreation / restart
        viewModelScope.launch {
            repository.ensureFocusSettingsInitialized()
            repository.seedDefaultAllowedAppsIfEmpty()

            // Observe enabled allowed apps count to keep pomodoroState updated
            launch {
                repository.getEnabledAllowedApps().collect { apps ->
                    _pomodoroState.value = _pomodoroState.value.copy(allowedAppsCount = apps.size)
                }
            }

            val initialSettings = repository.getFocusSettingsSync()
            if (initialSettings != null && initialSettings.isFocusModeActive) {
                val now = System.currentTimeMillis()
                val activeSession = repository.getActiveFocusSessionSync()
                val accumulatedActive = (activeSession?.activeDuration ?: 0L) * 1000L
                val accumulatedPaused = (activeSession?.pausedDuration ?: 0L) * 1000L
                val sessionStart = activeSession?.startTime ?: (now - accumulatedActive)

                if (initialSettings.isPaused) {
                    _pomodoroState.value = _pomodoroState.value.copy(
                        isRunning = false,
                        isPaused = true,
                        isBreak = initialSettings.isBreak,
                        totalSeconds = initialSettings.totalSeconds,
                        remainingSeconds = initialSettings.remainingSeconds,
                        selectedSubject = initialSettings.selectedSubjectName,
                        selectedChapter = initialSettings.selectedChapterName,
                        selectedTopic = initialSettings.selectedTopicName,
                        currentTaskName = initialSettings.selectedTopicName,
                        selectedDistractionShield = initialSettings.distractionShieldEnabled,
                        strictModeEnabled = initialSettings.strictModeEnabled,
                        ambientSoundType = initialSettings.ambientSoundType,
                        activeSessionId = initialSettings.activeSessionId,
                        sessionStartTime = sessionStart,
                        accumulatedActiveDurationMs = accumulatedActive,
                        accumulatedPausedDurationMs = accumulatedPaused,
                        lastPauseTimestamp = initialSettings.pausedAtTimestamp ?: now
                    )
                } else {
                    _pomodoroState.value = _pomodoroState.value.copy(
                        isRunning = true,
                        isPaused = false,
                        isBreak = initialSettings.isBreak,
                        totalSeconds = initialSettings.totalSeconds,
                        remainingSeconds = initialSettings.remainingSeconds,
                        selectedSubject = initialSettings.selectedSubjectName,
                        selectedChapter = initialSettings.selectedChapterName,
                        selectedTopic = initialSettings.selectedTopicName,
                        currentTaskName = initialSettings.selectedTopicName,
                        selectedDistractionShield = initialSettings.distractionShieldEnabled,
                        strictModeEnabled = initialSettings.strictModeEnabled,
                        ambientSoundType = initialSettings.ambientSoundType,
                        activeSessionId = initialSettings.activeSessionId,
                        sessionStartTime = sessionStart,
                        currentSegmentStartTime = now,
                        accumulatedActiveDurationMs = accumulatedActive,
                        accumulatedPausedDurationMs = accumulatedPaused,
                        lastPauseTimestamp = 0L
                    )
                    if (!initialSettings.isBreak && FocusPermissionManager.hasUsageAccessPermission(app)) {
                        val allowedApps = repository.getEnabledAllowedAppsSync().map { it.packageName }.toSet()
                        FocusEnforcementManager.startEnforcement(app, initialSettings.activeSessionId ?: 1L, allowedApps)
                    }
                    startFocusActiveTimerLoop()
                }
            }
        }

        // Observe active goal to load its specific subjects, chapters, lectures, and plan
        viewModelScope.launch {
            activeGoal.collectLatest { goal ->
                if (goal != null) {
                    launch { repository.getSubjectsForExam(goal.id).collect { _subjects.value = it } }
                    launch { repository.getAllChaptersForExam(goal.id).collect { _chapters.value = it } }
                    launch { repository.getAllLecturesForExam(goal.id).collect { _lectures.value = it } }
                    launch {
                        repository.getPlanForExam(goal.id).collect { plan ->
                            _studyPlan.value = plan
                            if (plan != null) {
                                repository.getTimetableForPlan(plan.id).collect { _timetable.value = it }
                            }
                        }
                    }
                } else {
                    if (authPrefs.getBoolean("is_logged_in", false)) {
                        val syncGoal = repository.getActiveGoalSync()
                        if (syncGoal == null && _currentScreen.value is AppScreen.Dashboard) {
                            _currentScreen.value = AppScreen.OnboardingSelectGoal
                        }
                    }
                }
            }
        }
    }

    private fun itChapterExcluded(chapterId: Long): Boolean {
        val ch = _chapters.value.find { it.id == chapterId }
        return ch?.isHidden == true || ch?.isExcludedFromPlan == true
    }

    // Onboarding Actions
    fun completeOnboarding(
        category: String,
        examName: String,
        year: String,
        days: Int = 90,
        customTargetTimestamp: Long? = null,
        board: String? = null,
        stream: String? = null,
        selectedSubjectNames: List<String>? = null,
        customSubjects: List<DefaultSubjectTemplate> = emptyList()
    ) {
        viewModelScope.launch {
            repository.initializeExamGoalWithDefaultSyllabus(
                category = category,
                examName = examName,
                year = year,
                targetDays = days,
                customTargetTimestamp = customTargetTimestamp,
                board = board,
                stream = stream,
                selectedSubjectNames = selectedSubjectNames,
                customSubjects = customSubjects
            )
            _currentScreen.value = AppScreen.Dashboard
        }
    }

    fun switchActiveGoal(goalId: Long) {
        viewModelScope.launch {
            repository.switchActiveGoal(goalId)
            // Reload syllabus and progress for newly active goal
            val currentPlan = repository.getPlanForExamSync(goalId)
            if (currentPlan == null) {
                // Initialize default plan if missing
                val subjects = repository.getSubjectsForExamSync(goalId)
                if (subjects.isNotEmpty()) {
                    repository.saveStudyPlan(StudyPlanEntity(examId = goalId, targetDays = 90))
                }
            }
        }
    }

    fun updateTargetDate(targetTimestamp: Long) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val diffDays = ((targetTimestamp - now) / (1000 * 60 * 60 * 24)).toInt().coerceAtLeast(1)
            val goal = activeGoal.value ?: repository.getActiveGoalSync()
            val currentPlan = _studyPlan.value ?: (if (goal != null) repository.getPlanForExamSync(goal.id) else null)
            val updated = if (currentPlan != null) {
                currentPlan.copy(targetDays = diffDays, targetDateTimestamp = targetTimestamp)
            } else if (goal != null) {
                StudyPlanEntity(examId = goal.id, targetDays = diffDays, targetDateTimestamp = targetTimestamp)
            } else null

            if (updated != null) {
                repository.updateStudyPlan(updated)
                _studyPlan.value = updated
            }
        }
    }

    fun resetAndReloadSyllabus() {
        viewModelScope.launch {
            repository.resetAndReloadSyllabusForActiveGoal()
        }
    }

    fun resetAllProgress() {
        viewModelScope.launch {
            repository.resetAllProgressForActiveGoal()
        }
    }

    // Lecture Tracking
    fun toggleLectureCompleted(lectureId: Long) {
        val lec = _lectures.value.find { it.id == lectureId } ?: return
        viewModelScope.launch {
            repository.setLectureCompleted(lectureId, !lec.isCompleted)
        }
    }

    fun toggleLectureImportant(lectureId: Long) {
        viewModelScope.launch {
            repository.toggleLectureImportant(lectureId)
        }
    }

    fun toggleLectureNotes(lectureId: Long) {
        val lec = _lectures.value.find { it.id == lectureId } ?: return
        viewModelScope.launch {
            repository.setLectureNotesCompleted(lectureId, !lec.notesCompleted)
        }
    }

    fun toggleLecturePractice(lectureId: Long) {
        val lec = _lectures.value.find { it.id == lectureId } ?: return
        viewModelScope.launch {
            repository.setLecturePracticeCompleted(lectureId, !lec.practiceCompleted)
        }
    }

    fun savePersonalNote(lectureId: Long, note: String) {
        viewModelScope.launch {
            repository.updateLectureNote(lectureId, note)
        }
    }

    // Syllabus Customization
    fun addCustomSubject(name: String, colorHex: String) {
        val goalId = activeGoal.value?.id ?: return
        viewModelScope.launch {
            repository.addSubject(goalId, name, colorHex)
        }
    }

    fun updateSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            repository.updateSubject(subject)
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }

    fun addChapter(subjectId: Long, name: String, lectureCount: Int, isImportant: Boolean = false) {
        viewModelScope.launch {
            repository.addChapter(subjectId, name, lectureCount, isImportant)
        }
    }

    fun updateChapter(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.updateChapter(chapter)
        }
    }

    fun toggleChapterHidden(chapterId: Long, isHidden: Boolean) {
        viewModelScope.launch {
            repository.toggleChapterHidden(chapterId, isHidden)
        }
    }

    fun toggleChapterImportant(chapterId: Long) {
        viewModelScope.launch {
            repository.toggleChapterImportant(chapterId)
        }
    }

    fun deleteChapter(chapter: ChapterEntity) {
        viewModelScope.launch {
            repository.deleteChapter(chapter)
        }
    }

    // Study Plan Customization
    fun updatePlaybackSpeed(speed: Float) {
        viewModelScope.launch {
            val goal = activeGoal.value ?: repository.getActiveGoalSync()
            val currentPlan = _studyPlan.value ?: (if (goal != null) repository.getPlanForExamSync(goal.id) else null)
            val updated = if (currentPlan != null) {
                currentPlan.copy(playbackSpeed = speed)
            } else if (goal != null) {
                StudyPlanEntity(examId = goal.id, playbackSpeed = speed)
            } else null

            if (updated != null) {
                repository.updateStudyPlan(updated)
                _studyPlan.value = updated
            }
        }
    }

    fun updateNotesTime(minutes: Int) {
        viewModelScope.launch {
            val goal = activeGoal.value ?: repository.getActiveGoalSync()
            val currentPlan = _studyPlan.value ?: (if (goal != null) repository.getPlanForExamSync(goal.id) else null)
            val updated = if (currentPlan != null) {
                currentPlan.copy(notesMinutesPerLecture = minutes)
            } else if (goal != null) {
                StudyPlanEntity(examId = goal.id, notesMinutesPerLecture = minutes)
            } else null

            if (updated != null) {
                repository.updateStudyPlan(updated)
                _studyPlan.value = updated
            }
        }
    }

    fun updatePracticeTime(minutes: Int) {
        viewModelScope.launch {
            val goal = activeGoal.value ?: repository.getActiveGoalSync()
            val currentPlan = _studyPlan.value ?: (if (goal != null) repository.getPlanForExamSync(goal.id) else null)
            val updated = if (currentPlan != null) {
                currentPlan.copy(practiceMinutesDaily = minutes)
            } else if (goal != null) {
                StudyPlanEntity(examId = goal.id, practiceMinutesDaily = minutes)
            } else null

            if (updated != null) {
                repository.updateStudyPlan(updated)
                _studyPlan.value = updated
            }
        }
    }

    fun updateRevisionTime(minutes: Int) {
        viewModelScope.launch {
            val goal = activeGoal.value ?: repository.getActiveGoalSync()
            val currentPlan = _studyPlan.value ?: (if (goal != null) repository.getPlanForExamSync(goal.id) else null)
            val updated = if (currentPlan != null) {
                currentPlan.copy(revisionMinutesDaily = minutes)
            } else if (goal != null) {
                StudyPlanEntity(examId = goal.id, revisionMinutesDaily = minutes)
            } else null

            if (updated != null) {
                repository.updateStudyPlan(updated)
                _studyPlan.value = updated
            }
        }
    }

    fun updateTargetDays(days: Int) {
        viewModelScope.launch {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, days)
            val goal = activeGoal.value ?: repository.getActiveGoalSync()
            val currentPlan = _studyPlan.value ?: (if (goal != null) repository.getPlanForExamSync(goal.id) else null)
            val updated = if (currentPlan != null) {
                currentPlan.copy(targetDays = days, targetDateTimestamp = cal.timeInMillis)
            } else if (goal != null) {
                StudyPlanEntity(examId = goal.id, targetDays = days, targetDateTimestamp = cal.timeInMillis)
            } else null

            if (updated != null) {
                repository.updateStudyPlan(updated)
                _studyPlan.value = updated
            }
        }
    }

    fun updateStudyPattern(pattern: String) {
        viewModelScope.launch {
            val goal = activeGoal.value ?: repository.getActiveGoalSync()
            val currentPlan = _studyPlan.value ?: (if (goal != null) repository.getPlanForExamSync(goal.id) else null)
            val updated = if (currentPlan != null) {
                currentPlan.copy(studyPattern = pattern)
            } else if (goal != null) {
                StudyPlanEntity(examId = goal.id, studyPattern = pattern)
            } else null

            if (updated != null) {
                repository.updateStudyPlan(updated)
                _studyPlan.value = updated
            }
        }
    }

    fun assignSubjectToDay(dayOfWeek: Int, subjectId: Long) {
        val planId = _studyPlan.value?.id ?: return
        val currentEntries = _timetable.value.filter { it.dayOfWeek != dayOfWeek }.toMutableList()
        currentEntries.add(WeeklyTimetableEntryEntity(planId = planId, dayOfWeek = dayOfWeek, subjectId = subjectId))
        viewModelScope.launch {
            repository.updateWeeklyTimetable(planId, currentEntries)
        }
    }

    fun applyMissedWorkload(hours: Float, mode: String) {
        val planId = _studyPlan.value?.id ?: return
        viewModelScope.launch {
            repository.applyMissedWorkloadAdjustment(planId, hours, mode)
        }
    }

    // Focus Mode & Pomodoro Timer Controls
    fun setPomodoroPreset(focusMinutes: Int, breakMinutes: Int) {
        timerJob?.cancel()
        _pomodoroState.value = _pomodoroState.value.copy(
            isRunning = false,
            isPaused = false,
            isBreak = false,
            totalSeconds = focusMinutes * 60,
            remainingSeconds = focusMinutes * 60,
            activeSessionId = null
        )
        viewModelScope.launch {
            repository.saveFocusSettings(
                FocusSettingsEntity(
                    id = 1,
                    isFocusModeActive = false,
                    isPaused = false,
                    activeSessionId = null,
                    targetEndTimeTimestamp = null,
                    totalSeconds = focusMinutes * 60,
                    remainingSeconds = focusMinutes * 60,
                    isBreak = false,
                    selectedSubjectName = _pomodoroState.value.selectedSubject,
                    selectedChapterName = _pomodoroState.value.selectedChapter,
                    selectedTopicName = _pomodoroState.value.selectedTopic,
                    distractionShieldEnabled = _pomodoroState.value.selectedDistractionShield,
                    strictModeEnabled = _pomodoroState.value.strictModeEnabled,
                    ambientSoundType = _pomodoroState.value.ambientSoundType
                )
            )
        }
    }

    fun startFocusSession(taskName: String = "") {
        if (_pomodoroState.value.isRunning && !_pomodoroState.value.isPaused) return

        // 1. Verify required Focus Mode permission
        if (!FocusPermissionManager.hasUsageAccessPermission(app)) {
            refreshFocusPermissions(app)
            navigateTo(AppScreen.FocusPermission)
            return
        }

        val state = _pomodoroState.value

        // If resuming a paused session
        if (state.isPaused && state.activeSessionId != null) {
            resumeFocusSession()
            return
        }

        val now = System.currentTimeMillis()
        val targetTask = if (taskName.isNotBlank()) {
            taskName
        } else if (state.selectedTopic.isNotBlank()) {
            state.selectedTopic
        } else if (state.selectedChapter.isNotBlank()) {
            "${state.selectedSubject} - ${state.selectedChapter}"
        } else {
            "${state.selectedSubject} Core Preparation"
        }

        viewModelScope.launch {
            val goalId = activeGoal.value?.id
            val subjectId = _subjects.value.find { it.name == state.selectedSubject }?.id

            val sessionId = repository.startFocusSession(
                goalId = goalId,
                subjectId = subjectId,
                subjectName = state.selectedSubject,
                chapterName = state.selectedChapter,
                topicName = targetTask,
                durationMinutes = (state.totalSeconds / 60).coerceAtLeast(1),
                sessionType = if (state.isBreak) "BREAK" else "FOCUS"
            )

            val allowedApps = repository.getEnabledAllowedAppsSync().map { it.packageName }.toSet()

            if (!state.isBreak) {
                FocusEnforcementManager.startEnforcement(app, sessionId, allowedApps)
            }

            _pomodoroState.value = state.copy(
                isRunning = true,
                isPaused = false,
                activeSessionId = sessionId,
                currentTaskName = targetTask,
                sessionStartTime = now,
                currentSegmentStartTime = now,
                accumulatedActiveDurationMs = 0L,
                accumulatedPausedDurationMs = 0L,
                lastPauseTimestamp = 0L,
                allowedAppsCount = allowedApps.size
            )

            repository.saveFocusSettings(
                FocusSettingsEntity(
                    id = 1,
                    isFocusModeActive = true,
                    isPaused = false,
                    activeSessionId = sessionId,
                    targetEndTimeTimestamp = now + (state.remainingSeconds * 1000L),
                    totalSeconds = state.totalSeconds,
                    remainingSeconds = state.remainingSeconds,
                    isBreak = state.isBreak,
                    selectedGoalId = goalId,
                    selectedSubjectName = state.selectedSubject,
                    selectedChapterName = state.selectedChapter,
                    selectedTopicName = targetTask,
                    distractionShieldEnabled = state.selectedDistractionShield,
                    strictModeEnabled = state.strictModeEnabled,
                    ambientSoundType = state.ambientSoundType
                )
            )

            startFocusActiveTimerLoop()
        }
    }

    private fun startFocusActiveTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var tick = 0
            while (_pomodoroState.value.isRunning && !_pomodoroState.value.isPaused) {
                delay(1000)
                tick++
                val state = _pomodoroState.value
                val activeSecs = state.getActiveDurationSeconds()

                // Trigger UI recomposition with actual system timestamp
                _pomodoroState.value = state.copy(
                    elapsedSecondsTicker = System.currentTimeMillis()
                )

                // Periodically persist progress to Room DB (every 10 seconds)
                if (tick % 10 == 0) {
                    val sessionId = state.activeSessionId
                    if (sessionId != null) {
                        repository.pauseFocusSession(
                            sessionId = sessionId,
                            activeDurationSeconds = activeSecs,
                            pausedDurationSeconds = state.getPausedDurationSeconds()
                        )
                    }
                }
            }
        }
    }

    fun pauseFocusSession() {
        timerJob?.cancel()
        val state = _pomodoroState.value
        if (!state.isRunning || state.isPaused) return

        val now = System.currentTimeMillis()
        val currentSegmentDuration = (now - state.currentSegmentStartTime).coerceAtLeast(0L)
        val newAccumulatedActive = state.accumulatedActiveDurationMs + currentSegmentDuration

        FocusEnforcementManager.pauseEnforcement(app)

        _pomodoroState.value = state.copy(
            isRunning = false,
            isPaused = true,
            accumulatedActiveDurationMs = newAccumulatedActive,
            lastPauseTimestamp = now
        )

        viewModelScope.launch {
            val sessionId = state.activeSessionId
            if (sessionId != null) {
                repository.pauseFocusSession(
                    sessionId = sessionId,
                    activeDurationSeconds = newAccumulatedActive / 1000L,
                    pausedDurationSeconds = state.accumulatedPausedDurationMs / 1000L
                )
            }
            repository.updateFocusTimerState(
                isActive = true,
                isPaused = true,
                remainingSeconds = state.remainingSeconds,
                targetEndTime = null
            )
        }
    }

    fun resumeFocusSession() {
        val state = _pomodoroState.value
        if (state.isRunning || !state.isPaused) return

        // Validate permission still active
        if (!FocusPermissionManager.hasUsageAccessPermission(app)) {
            refreshFocusPermissions(app)
            navigateTo(AppScreen.FocusPermission)
            return
        }

        val now = System.currentTimeMillis()
        val pauseDuration = if (state.lastPauseTimestamp > 0L) (now - state.lastPauseTimestamp).coerceAtLeast(0L) else 0L
        val newAccumulatedPaused = state.accumulatedPausedDurationMs + pauseDuration

        FocusEnforcementManager.resumeEnforcement(app)

        _pomodoroState.value = state.copy(
            isRunning = true,
            isPaused = false,
            currentSegmentStartTime = now,
            accumulatedPausedDurationMs = newAccumulatedPaused,
            lastPauseTimestamp = 0L
        )

        viewModelScope.launch {
            val sessionId = state.activeSessionId
            if (sessionId != null) {
                repository.resumeFocusSession(sessionId)
            }
            startFocusActiveTimerLoop()
        }
    }

    fun endFocusSession(isAbandoned: Boolean = false) {
        timerJob?.cancel()
        val state = _pomodoroState.value
        val sessionId = state.activeSessionId
        val activeSecs = state.getActiveDurationSeconds()
        val pausedSecs = state.getPausedDurationSeconds()

        FocusEnforcementManager.stopEnforcement(app)

        val activeMins = (activeSecs / 60).toInt()

        _pomodoroState.value = state.copy(
            isRunning = false,
            isPaused = false,
            activeSessionId = null,
            sessionStartTime = 0L,
            currentSegmentStartTime = 0L,
            accumulatedActiveDurationMs = 0L,
            accumulatedPausedDurationMs = 0L,
            lastPauseTimestamp = 0L,
            completedFocusMinutesToday = state.completedFocusMinutesToday + activeMins
        )

        viewModelScope.launch {
            if (sessionId != null) {
                if (isAbandoned && activeSecs < 60) {
                    repository.abandonFocusSession(sessionId, activeSecs, pausedSecs)
                } else {
                    repository.completeFocusSession(sessionId, activeSecs, pausedSecs)
                }
            }

            repository.saveFocusSettings(
                FocusSettingsEntity(
                    id = 1,
                    isFocusModeActive = false,
                    isPaused = false,
                    activeSessionId = null,
                    targetEndTimeTimestamp = null,
                    totalSeconds = state.totalSeconds,
                    remainingSeconds = state.totalSeconds,
                    isBreak = state.isBreak,
                    selectedSubjectName = state.selectedSubject,
                    selectedChapterName = state.selectedChapter,
                    selectedTopicName = state.selectedTopic,
                    distractionShieldEnabled = state.selectedDistractionShield,
                    strictModeEnabled = state.strictModeEnabled,
                    ambientSoundType = state.ambientSoundType
                )
            )
        }
    }

    fun startPomodoro() {
        startFocusSession()
    }

    fun pausePomodoro() {
        pauseFocusSession()
    }

    fun resumePomodoro() {
        resumeFocusSession()
    }

    fun resetPomodoro() {
        endFocusSession(isAbandoned = true)
    }

    fun skipBreak() {
        timerJob?.cancel()
        _pomodoroState.value = _pomodoroState.value.copy(
            isRunning = false,
            isPaused = false,
            isBreak = false,
            totalSeconds = 25 * 60,
            remainingSeconds = 25 * 60,
            activeSessionId = null
        )
        viewModelScope.launch {
            repository.saveFocusSettings(
                FocusSettingsEntity(
                    id = 1,
                    isFocusModeActive = false,
                    isPaused = false,
                    activeSessionId = null,
                    targetEndTimeTimestamp = null,
                    totalSeconds = 25 * 60,
                    remainingSeconds = 25 * 60,
                    isBreak = false,
                    selectedSubjectName = _pomodoroState.value.selectedSubject,
                    selectedChapterName = _pomodoroState.value.selectedChapter,
                    selectedTopicName = _pomodoroState.value.selectedTopic,
                    distractionShieldEnabled = _pomodoroState.value.selectedDistractionShield,
                    strictModeEnabled = _pomodoroState.value.strictModeEnabled,
                    ambientSoundType = _pomodoroState.value.ambientSoundType
                )
            )
        }
    }

    fun setPomodoroTag(subject: String, chapter: String = "", topic: String = "") {
        _pomodoroState.value = _pomodoroState.value.copy(
            selectedSubject = subject,
            selectedChapter = chapter,
            selectedTopic = topic
        )
        viewModelScope.launch {
            val current = repository.getFocusSettingsSync() ?: FocusSettingsEntity(id = 1)
            repository.saveFocusSettings(
                current.copy(
                    selectedSubjectName = subject,
                    selectedChapterName = chapter,
                    selectedTopicName = topic
                )
            )
        }
    }

    fun toggleDistractionShield() {
        val updated = !_pomodoroState.value.selectedDistractionShield
        _pomodoroState.value = _pomodoroState.value.copy(
            selectedDistractionShield = updated
        )
        viewModelScope.launch {
            val current = repository.getFocusSettingsSync() ?: FocusSettingsEntity(id = 1)
            repository.saveFocusSettings(current.copy(distractionShieldEnabled = updated))
        }
    }

    private fun completePomodoroSession() {
        val state = _pomodoroState.value
        val sessionId = state.activeSessionId
        val sessionMinutes = (state.totalSeconds / 60).coerceAtLeast(1)
        val nextIsBreak = !state.isBreak
        val nextMinutes = if (nextIsBreak) 5 else 25

        // Stop real Android enforcement
        FocusEnforcementManager.stopEnforcement(app)

        _pomodoroState.value = state.copy(
            isRunning = false,
            isPaused = false,
            isBreak = nextIsBreak,
            totalSeconds = nextMinutes * 60,
            remainingSeconds = nextMinutes * 60,
            completedFocusMinutesToday = state.completedFocusMinutesToday + if (!state.isBreak) sessionMinutes else 0,
            activeSessionId = null
        )

        viewModelScope.launch {
            if (sessionId != null) {
                repository.completeFocusSession(
                    sessionId = sessionId,
                    activeDurationSeconds = state.totalSeconds.toLong(),
                    pausedDurationSeconds = 0L
                )
            } else if (!state.isBreak) {
                repository.logFocusSession(
                    subjectName = state.selectedSubject,
                    chapterName = state.selectedChapter,
                    topicName = state.selectedTopic,
                    durationMinutes = sessionMinutes,
                    sessionType = "FOCUS"
                )
            }

            repository.saveFocusSettings(
                FocusSettingsEntity(
                    id = 1,
                    isFocusModeActive = false,
                    isPaused = false,
                    activeSessionId = null,
                    targetEndTimeTimestamp = null,
                    totalSeconds = nextMinutes * 60,
                    remainingSeconds = nextMinutes * 60,
                    isBreak = nextIsBreak,
                    selectedSubjectName = state.selectedSubject,
                    selectedChapterName = state.selectedChapter,
                    selectedTopicName = state.selectedTopic,
                    distractionShieldEnabled = state.selectedDistractionShield,
                    strictModeEnabled = state.strictModeEnabled,
                    ambientSoundType = state.ambientSoundType
                )
            )
        }
    }

    private fun completeRestoredSession(settings: FocusSettingsEntity) {
        val sessionId = settings.activeSessionId
        val durationMinutes = (settings.totalSeconds / 60).coerceAtLeast(1)
        val nextIsBreak = !settings.isBreak
        val nextMinutes = if (nextIsBreak) 5 else 25

        // Stop real Android enforcement
        FocusEnforcementManager.stopEnforcement(app)

        _pomodoroState.value = _pomodoroState.value.copy(
            isRunning = false,
            isPaused = false,
            isBreak = nextIsBreak,
            totalSeconds = nextMinutes * 60,
            remainingSeconds = nextMinutes * 60,
            completedFocusMinutesToday = _pomodoroState.value.completedFocusMinutesToday + if (!settings.isBreak) durationMinutes else 0,
            activeSessionId = null
        )

        viewModelScope.launch {
            if (sessionId != null) {
                repository.completeFocusSession(
                    sessionId = sessionId,
                    activeDurationSeconds = settings.totalSeconds.toLong(),
                    pausedDurationSeconds = 0L
                )
            } else if (!settings.isBreak) {
                repository.logFocusSession(
                    subjectName = settings.selectedSubjectName,
                    chapterName = settings.selectedChapterName,
                    topicName = settings.selectedTopicName,
                    durationMinutes = durationMinutes,
                    sessionType = "FOCUS"
                )
            }

            repository.saveFocusSettings(
                FocusSettingsEntity(
                    id = 1,
                    isFocusModeActive = false,
                    isPaused = false,
                    activeSessionId = null,
                    targetEndTimeTimestamp = null,
                    totalSeconds = nextMinutes * 60,
                    remainingSeconds = nextMinutes * 60,
                    isBreak = nextIsBreak,
                    selectedSubjectName = settings.selectedSubjectName,
                    selectedChapterName = settings.selectedChapterName,
                    selectedTopicName = settings.selectedTopicName,
                    distractionShieldEnabled = settings.distractionShieldEnabled,
                    strictModeEnabled = settings.strictModeEnabled,
                    ambientSoundType = settings.ambientSoundType
                )
            )
        }
    }

    // Focus Allowed Apps Controls
    fun toggleAllowedApp(packageName: String, isEnabled: Boolean) {
        viewModelScope.launch {
            repository.toggleAppAllowed(packageName, isEnabled)
        }
    }

    fun addAllowedApp(packageName: String, appName: String, category: String = "EDUCATION") {
        viewModelScope.launch {
            repository.setAppAllowed(packageName, appName, isAllowed = true, category = category)
        }
    }

    fun removeAllowedApp(packageName: String) {
        viewModelScope.launch {
            repository.removeAllowedApp(packageName)
        }
    }

    fun setStrictMode(enabled: Boolean) {
        _pomodoroState.value = _pomodoroState.value.copy(strictModeEnabled = enabled)
        viewModelScope.launch {
            val current = repository.getFocusSettingsSync() ?: FocusSettingsEntity(id = 1)
            repository.saveFocusSettings(current.copy(strictModeEnabled = enabled))
        }
    }

    fun setAmbientSound(soundType: String) {
        _pomodoroState.value = _pomodoroState.value.copy(ambientSoundType = soundType)
        viewModelScope.launch {
            val current = repository.getFocusSettingsSync() ?: FocusSettingsEntity(id = 1)
            repository.saveFocusSettings(current.copy(ambientSoundType = soundType))
        }
    }

    // ==========================================
    // Focus Mode Permission Management (Phase 3)
    // ==========================================

    fun refreshFocusPermissions(context: Context) {
        val wasEverGranted = authPrefs.getBoolean("focus_permission_ever_granted", false)
        val hasAttempted = authPrefs.getBoolean("focus_permission_attempted", false)
        val newState = FocusPermissionManager.checkPermissionState(
            context,
            wasPreviouslyGranted = wasEverGranted,
            hasAttemptedGrant = hasAttempted
        )

        if (newState.isGranted) {
            authPrefs.edit().putBoolean("focus_permission_ever_granted", true).apply()
        } else if (newState.isRevoked) {
            // Permission was revoked later in Android Settings!
            // Safely disable enforcement rather than pretending Focus Mode is active.
            if (_pomodoroState.value.isRunning) {
                pausePomodoro()
            }
            viewModelScope.launch {
                val current = repository.getFocusSettingsSync() ?: FocusSettingsEntity(id = 1)
                repository.saveFocusSettings(
                    current.copy(
                        isFocusModeActive = false,
                        isPaused = false,
                        targetEndTimeTimestamp = null
                    )
                )
            }
        }
        _focusPermissionState.value = newState
    }

    fun onAllowPermissionClicked(context: Context) {
        authPrefs.edit().putBoolean("focus_permission_attempted", true).apply()
        FocusPermissionManager.openRequiredSettings(context)
        // Refresh immediately after opening settings attempt
        val wasEverGranted = authPrefs.getBoolean("focus_permission_ever_granted", false)
        _focusPermissionState.value = FocusPermissionManager.checkPermissionState(
            context,
            wasPreviouslyGranted = wasEverGranted,
            hasAttemptedGrant = true
        )
    }

    // ==========================================
    // Focus Mode: Real Allowed Apps Selection (Phase 4)
    // ==========================================

    fun loadDeviceApps(context: Context) {
        viewModelScope.launch {
            _isInstalledAppsLoading.value = true
            val apps = withContext(Dispatchers.IO) {
                InstalledAppsManager.getInstalledLaunchableApps(context)
            }
            _installedDeviceApps.value = apps
            _isInstalledAppsLoading.value = false
        }
    }

    fun toggleAllowedAppSelection(packageName: String, appName: String, isAllowed: Boolean) {
        viewModelScope.launch {
            repository.setAppAllowed(
                packageName = packageName,
                appName = appName,
                isAllowed = isAllowed,
                category = "DEVICE_APP"
            )
            // Synchronize Pomodoro UI state count
            val currentAllowed = repository.getEnabledAllowedAppsSync()
            _pomodoroState.value = _pomodoroState.value.copy(
                allowedAppsCount = currentAllowed.size
            )
        }
    }

    fun clearAllAllowedApps() {
        viewModelScope.launch {
            val currentAllowed = repository.getEnabledAllowedAppsSync()
            for (app in currentAllowed) {
                repository.removeAllowedApp(app.packageName)
            }
            _pomodoroState.value = _pomodoroState.value.copy(allowedAppsCount = 0)
        }
    }

    fun saveAllowedAppsConfiguration() {
        viewModelScope.launch {
            val currentAllowed = repository.getEnabledAllowedAppsSync()
            _pomodoroState.value = _pomodoroState.value.copy(
                allowedAppsCount = currentAllowed.size
            )
        }
    }

    fun resetEntirePlanAndSyllabus() {
        val goal = activeGoal.value ?: return
        viewModelScope.launch {
            repository.initializeExamGoalWithDefaultSyllabus(
                category = goal.category,
                examName = goal.examName,
                year = goal.targetYear,
                targetDays = 90
            )
        }
    }
}
