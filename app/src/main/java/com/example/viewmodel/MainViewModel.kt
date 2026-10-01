package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.defaultdata.DefaultSyllabusCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.entity.ChapterEntity
import com.example.data.local.entity.ExamGoalEntity
import com.example.data.local.entity.FocusSessionEntity
import com.example.data.local.entity.LectureEntity
import com.example.data.local.entity.StudyPlanEntity
import com.example.data.local.entity.SubjectEntity
import com.example.data.local.entity.UserStreakEntity
import com.example.data.local.entity.WeeklyTimetableEntryEntity
import com.example.data.model.ActivationResult
import com.example.data.model.StudyPlanCalculation
import com.example.data.model.SubscriptionInfo
import com.example.data.model.SubscriptionTier
import com.example.data.repository.PrepTrackRepository
import com.example.data.repository.SubscriptionRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
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
    val isBreak: Boolean = false,
    val totalSeconds: Int = 25 * 60,
    val remainingSeconds: Int = 25 * 60,
    val selectedSubject: String = "Physics",
    val selectedChapter: String = "",
    val selectedTopic: String = "",
    val completedFocusMinutesToday: Int = 0,
    val selectedDistractionShield: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PrepTrackRepository
    private val authPrefs = application.getSharedPreferences("prep_track_auth", android.content.Context.MODE_PRIVATE)
    private val subscriptionRepository = SubscriptionRepository(application)
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
        val database = AppDatabase.getInstance(application)
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

    // Pomodoro Timer State
    private val _pomodoroState = MutableStateFlow(PomodoroUiState())
    val pomodoroState: StateFlow<PomodoroUiState> = _pomodoroState.asStateFlow()
    private var timerJob: Job? = null

    init {
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
        customTargetTimestamp: Long? = null
    ) {
        viewModelScope.launch {
            repository.initializeExamGoalWithDefaultSyllabus(category, examName, year, days, customTargetTimestamp)
            _currentScreen.value = AppScreen.Dashboard
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

    // Pomodoro Timer Controls
    fun setPomodoroPreset(focusMinutes: Int, breakMinutes: Int) {
        timerJob?.cancel()
        _pomodoroState.value = _pomodoroState.value.copy(
            isRunning = false,
            isBreak = false,
            totalSeconds = focusMinutes * 60,
            remainingSeconds = focusMinutes * 60
        )
    }

    fun startPomodoro() {
        if (_pomodoroState.value.isRunning) return
        _pomodoroState.value = _pomodoroState.value.copy(isRunning = true)
        timerJob = viewModelScope.launch {
            while (_pomodoroState.value.remainingSeconds > 0 && _pomodoroState.value.isRunning) {
                delay(1000)
                val remaining = _pomodoroState.value.remainingSeconds - 1
                _pomodoroState.value = _pomodoroState.value.copy(remainingSeconds = remaining)
            }
            if (_pomodoroState.value.remainingSeconds <= 0) {
                completePomodoroSession()
            }
        }
    }

    fun pausePomodoro() {
        timerJob?.cancel()
        _pomodoroState.value = _pomodoroState.value.copy(isRunning = false)
    }

    fun resetPomodoro() {
        timerJob?.cancel()
        _pomodoroState.value = _pomodoroState.value.copy(
            isRunning = false,
            remainingSeconds = _pomodoroState.value.totalSeconds
        )
    }

    fun skipBreak() {
        timerJob?.cancel()
        _pomodoroState.value = _pomodoroState.value.copy(
            isRunning = false,
            isBreak = false,
            totalSeconds = 25 * 60,
            remainingSeconds = 25 * 60
        )
    }

    fun setPomodoroTag(subject: String, chapter: String = "", topic: String = "") {
        _pomodoroState.value = _pomodoroState.value.copy(
            selectedSubject = subject,
            selectedChapter = chapter,
            selectedTopic = topic
        )
    }

    fun toggleDistractionShield() {
        _pomodoroState.value = _pomodoroState.value.copy(
            selectedDistractionShield = !_pomodoroState.value.selectedDistractionShield
        )
    }

    private fun completePomodoroSession() {
        val state = _pomodoroState.value
        val sessionMinutes = state.totalSeconds / 60
        val sessionType = if (state.isBreak) "BREAK" else "FOCUS"

        viewModelScope.launch {
            if (!state.isBreak) {
                repository.logFocusSession(
                    subjectName = state.selectedSubject,
                    chapterName = state.selectedChapter,
                    topicName = state.selectedTopic,
                    durationMinutes = sessionMinutes,
                    sessionType = "FOCUS"
                )
            }

            // Switch to break or focus
            val nextIsBreak = !state.isBreak
            val nextMinutes = if (nextIsBreak) 5 else 25
            _pomodoroState.value = state.copy(
                isRunning = false,
                isBreak = nextIsBreak,
                totalSeconds = nextMinutes * 60,
                remainingSeconds = nextMinutes * 60,
                completedFocusMinutesToday = state.completedFocusMinutesToday + if (!state.isBreak) sessionMinutes else 0
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
