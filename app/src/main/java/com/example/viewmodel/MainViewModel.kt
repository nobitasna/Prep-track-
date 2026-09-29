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
import com.example.data.model.StudyPlanCalculation
import com.example.data.repository.PrepTrackRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

sealed class AppScreen {
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

    init {
        val database = AppDatabase.getInstance(application)
        repository = PrepTrackRepository(database)
    }

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Dashboard)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<AppScreen>()

    fun navigateTo(screen: AppScreen) {
        _screenHistory.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            _currentScreen.value = _screenHistory.removeAt(_screenHistory.size - 1)
            return true
        }
        return false
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

        // Pick upcoming incomplete lectures from assigned subjects (up to 4-5 tasks for today)
        val tasks = mutableListOf<TodayTaskItem>()
        val filteredChaps = chaps.filter { it.subjectId in assignedSubjectIds && !it.isHidden }

        for (chap in filteredChaps) {
            val pendingLecs = lecs.filter { it.chapterId == chap.id && !it.isCompleted }.take(2)
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
                if (tasks.size >= 4) break
            }
            if (tasks.size >= 4) break
        }

        // If all completed or no tasks from assigned, pick any first 2 pending from syllabus
        if (tasks.isEmpty()) {
            val firstPending = lecs.filter { !it.isCompleted }.take(2)
            for (lec in firstPending) {
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

    // Pomodoro Timer State
    private val _pomodoroState = MutableStateFlow(PomodoroUiState())
    val pomodoroState: StateFlow<PomodoroUiState> = _pomodoroState.asStateFlow()
    private var timerJob: Job? = null

    init {
        // Observe active goal to load its specific subjects, chapters, lectures, and plan
        viewModelScope.launch {
            activeGoal.collect { goal ->
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
                    // Check if any goals exist in database, if none show onboarding
                    val syncGoal = repository.getActiveGoalSync()
                    if (syncGoal == null) {
                        _currentScreen.value = AppScreen.OnboardingWelcome
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
    fun completeOnboarding(category: String, examName: String, year: String, days: Int = 90) {
        viewModelScope.launch {
            repository.initializeExamGoalWithDefaultSyllabus(category, examName, year, days)
            _currentScreen.value = AppScreen.Dashboard
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
        val plan = _studyPlan.value ?: return
        viewModelScope.launch {
            repository.updateStudyPlan(plan.copy(playbackSpeed = speed))
        }
    }

    fun updateNotesTime(minutes: Int) {
        val plan = _studyPlan.value ?: return
        viewModelScope.launch {
            repository.updateStudyPlan(plan.copy(notesMinutesPerLecture = minutes))
        }
    }

    fun updatePracticeTime(minutes: Int) {
        val plan = _studyPlan.value ?: return
        viewModelScope.launch {
            repository.updateStudyPlan(plan.copy(practiceMinutesDaily = minutes))
        }
    }

    fun updateRevisionTime(minutes: Int) {
        val plan = _studyPlan.value ?: return
        viewModelScope.launch {
            repository.updateStudyPlan(plan.copy(revisionMinutesDaily = minutes))
        }
    }

    fun updateTargetDays(days: Int) {
        val plan = _studyPlan.value ?: return
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, days)
        viewModelScope.launch {
            repository.updateStudyPlan(plan.copy(targetDays = days, targetDateTimestamp = cal.timeInMillis))
        }
    }

    fun updateStudyPattern(pattern: String) {
        val plan = _studyPlan.value ?: return
        viewModelScope.launch {
            repository.updateStudyPlan(plan.copy(studyPattern = pattern))
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
