package com.example.data.repository

import com.example.data.defaultdata.DefaultSubjectTemplate
import com.example.data.defaultdata.DefaultSyllabusCatalog
import com.example.data.defaultdata.StreamSyllabusCatalog
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PrepTrackRepository(private val database: AppDatabase) {

    private val examGoalDao = database.examGoalDao()
    private val subjectDao = database.subjectDao()
    private val chapterDao = database.chapterDao()
    private val lectureDao = database.lectureDao()
    private val studyPlanDao = database.studyPlanDao()
    private val weeklyTimetableDao = database.weeklyTimetableDao()
    private val focusSessionDao = database.focusSessionDao()
    private val focusSettingsDao = database.focusSettingsDao()
    private val focusAllowedAppDao = database.focusAllowedAppDao()
    private val userStreakDao = database.userStreakDao()

    // Active Goal & Plans
    fun getActiveGoal(): Flow<ExamGoalEntity?> = examGoalDao.getActiveGoal()
    fun getAllGoals(): Flow<List<ExamGoalEntity>> = examGoalDao.getAllGoals()
    suspend fun getActiveGoalSync(): ExamGoalEntity? = examGoalDao.getActiveGoalSync()

    fun getSubjectsForExam(examId: Long): Flow<List<SubjectEntity>> = subjectDao.getSubjectsForExam(examId)
    suspend fun getSubjectsForExamSync(examId: Long): List<SubjectEntity> = subjectDao.getSubjectsForExamSync(examId)

    fun getChaptersForSubject(subjectId: Long): Flow<List<ChapterEntity>> = chapterDao.getChaptersForSubject(subjectId)
    fun getAllChaptersForExam(examId: Long): Flow<List<ChapterEntity>> = chapterDao.getAllChaptersForExam(examId)
    suspend fun getAllChaptersForExamSync(examId: Long): List<ChapterEntity> = chapterDao.getAllChaptersForExamSync(examId)

    fun getLecturesForChapter(chapterId: Long): Flow<List<LectureEntity>> = lectureDao.getLecturesForChapter(chapterId)
    fun getAllLecturesForExam(examId: Long): Flow<List<LectureEntity>> = lectureDao.getAllLecturesForExam(examId)
    suspend fun getAllLecturesForExamSync(examId: Long): List<LectureEntity> = lectureDao.getAllLecturesForExamSync(examId)

    fun getPlanForExam(examId: Long): Flow<StudyPlanEntity?> = studyPlanDao.getPlanForExam(examId)
    suspend fun getPlanForExamSync(examId: Long): StudyPlanEntity? = studyPlanDao.getPlanForExamSync(examId)

    fun getTimetableForPlan(planId: Long): Flow<List<WeeklyTimetableEntryEntity>> = weeklyTimetableDao.getTimetableForPlan(planId)

    fun getAllFocusSessions(): Flow<List<FocusSessionEntity>> = focusSessionDao.getAllSessions()
    fun getFocusMinutesSince(sinceTimestamp: Long): Flow<Long?> = focusSessionDao.getFocusMinutesSince(sinceTimestamp)
    fun getTotalFocusMinutes(): Flow<Long?> = focusSessionDao.getTotalFocusMinutes()

    fun getUserStreak(): Flow<UserStreakEntity?> = userStreakDao.getStreak()

    /**
     * Initializes a new user goal and copies the selected/stream syllabus into a user-owned copy.
     * GLOBAL DEFAULT SYLLABUS -> USER SYLLABUS COPY -> USER CUSTOMIZATION
     */
    suspend fun initializeExamGoalWithDefaultSyllabus(
        category: String,
        examName: String,
        year: String,
        targetDays: Int = 90,
        customTargetTimestamp: Long? = null,
        board: String? = null,
        stream: String? = null,
        selectedSubjectNames: List<String>? = null,
        customSubjects: List<DefaultSubjectTemplate> = emptyList()
    ): Long {
        examGoalDao.deactivateAllGoals()

        val finalTargetTimestamp = customTargetTimestamp ?: run {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, targetDays)
            cal.timeInMillis
        }

        val fullExamName = if (!stream.isNullOrBlank() && !examName.contains(stream, ignoreCase = true)) {
            "$examName • $stream"
        } else {
            examName
        }

        val newGoal = ExamGoalEntity(
            category = category,
            examName = fullExamName,
            targetYear = year,
            targetDateTimestamp = finalTargetTimestamp,
            isActive = true
        )
        val goalId = examGoalDao.insertGoal(newGoal)

        // Resolve subject templates
        val subjectsToLoad = mutableListOf<DefaultSubjectTemplate>()

        if (!selectedSubjectNames.isNullOrEmpty()) {
            selectedSubjectNames.forEach { sName ->
                // First check in customSubjects
                val customMatch = customSubjects.find { it.name.equals(sName, ignoreCase = true) }
                if (customMatch != null) {
                    subjectsToLoad.add(customMatch)
                } else {
                    // Try StreamSyllabusCatalog
                    val catSubject = StreamSyllabusCatalog.getSubjectTemplate(sName, examName)
                    subjectsToLoad.add(catSubject)
                }
            }
        } else {
            // Fallback to pre-built DefaultExamTemplate
            val template = DefaultSyllabusCatalog.allTemplates.find {
                it.examName.equals(examName, ignoreCase = true)
            } ?: DefaultSyllabusCatalog.allTemplates.find {
                it.category.equals(category, ignoreCase = true)
            } ?: DefaultSyllabusCatalog.allTemplates.first()

            subjectsToLoad.addAll(template.subjects)
            subjectsToLoad.addAll(customSubjects)
        }

        // Copy template subjects into user's DB
        subjectsToLoad.forEachIndexed { sIndex, sTemplate ->
            val isCustomSub = customSubjects.any { it.name.equals(sTemplate.name, ignoreCase = true) }
            val subjectEntity = SubjectEntity(
                examId = goalId,
                name = sTemplate.name,
                colorHex = sTemplate.colorHex,
                iconName = sTemplate.iconName,
                sortOrder = sIndex,
                isCustom = isCustomSub
            )
            val subjectId = subjectDao.insertSubject(subjectEntity)

            sTemplate.chapters.forEachIndexed { cIndex, cTemplate ->
                val chapterEntity = ChapterEntity(
                    subjectId = subjectId,
                    name = cTemplate.name,
                    totalLectures = cTemplate.lectureCount,
                    sortOrder = cIndex,
                    isImportant = cTemplate.isImportant,
                    isCustom = isCustomSub
                )
                val chapterId = chapterDao.insertChapter(chapterEntity)

                // Populate individual lectures for each chapter with FIXED 2-HOUR reference
                val lectures = (1..cTemplate.lectureCount).map { lNum ->
                    LectureEntity(
                        chapterId = chapterId,
                        lectureNumber = lNum,
                        title = "Lecture $lNum: ${cTemplate.name.take(24)} (Part $lNum)",
                        isCompleted = false,
                        isImportant = cTemplate.isImportant && lNum == 1
                    )
                }
                lectureDao.insertLectures(lectures)
            }
        }

        // Initialize default study plan
        val studyPlan = StudyPlanEntity(
            examId = goalId,
            planScope = "ENTIRE",
            finishMode = "DAYS",
            targetDays = targetDays,
            targetDateTimestamp = finalTargetTimestamp,
            playbackSpeed = 1.0f,
            notesMinutesPerLecture = 30,
            practiceMinutesDaily = 60,
            revisionMinutesDaily = 30,
            studyPattern = "WEEKLY_TIMETABLE"
        )
        val planId = studyPlanDao.insertPlan(studyPlan)

        // Generate initial default weekly timetable for all subjects
        val subjects = subjectDao.getSubjectsForExamSync(goalId)
        if (subjects.isNotEmpty()) {
            val timetableEntries = mutableListOf<WeeklyTimetableEntryEntity>()
            // Distribute subjects across Mon(1) to Sun(7)
            for (day in 1..7) {
                val assignedSubject = subjects[(day - 1) % subjects.size]
                timetableEntries.add(
                    WeeklyTimetableEntryEntity(
                        planId = planId,
                        dayOfWeek = day,
                        subjectId = assignedSubject.id
                    )
                )
            }
            weeklyTimetableDao.insertEntries(timetableEntries)
        }

        // Ensure streak entry exists
        if (userStreakDao.getStreakSync() == null) {
            userStreakDao.insertOrUpdate(
                UserStreakEntity(
                    id = 1L,
                    currentStreak = 1,
                    longestStreak = 1,
                    lastStudyDateString = getTodayDateString(),
                    totalFocusMinutes = 0L,
                    totalLecturesCompleted = 0
                )
            )
        }

        return goalId
    }

    // Syllabus Management
    suspend fun addSubject(examId: Long, name: String, colorHex: String = "#1A73E8"): Long {
        val existing = subjectDao.getSubjectsForExamSync(examId)
        val subject = SubjectEntity(
            examId = examId,
            name = name,
            colorHex = colorHex,
            sortOrder = existing.size,
            isCustom = true
        )
        return subjectDao.insertSubject(subject)
    }

    suspend fun updateSubject(subject: SubjectEntity) {
        subjectDao.updateSubject(subject)
    }

    suspend fun deleteSubject(subject: SubjectEntity) {
        subjectDao.deleteSubject(subject)
    }

    suspend fun addChapter(subjectId: Long, name: String, lectureCount: Int, isImportant: Boolean = false): Long {
        val existing = chapterDao.getChaptersForSubjectSync(subjectId)
        val chapter = ChapterEntity(
            subjectId = subjectId,
            name = name,
            totalLectures = lectureCount.coerceAtLeast(1),
            sortOrder = existing.size,
            isImportant = isImportant,
            isCustom = true
        )
        val chapterId = chapterDao.insertChapter(chapter)

        val lectures = (1..chapter.totalLectures).map { lNum ->
            LectureEntity(
                chapterId = chapterId,
                lectureNumber = lNum,
                title = "Lecture $lNum: ${name.take(24)}",
                isCompleted = false
            )
        }
        lectureDao.insertLectures(lectures)
        return chapterId
    }

    suspend fun updateChapter(chapter: ChapterEntity) {
        val old = chapterDao.getChapterById(chapter.id)
        chapterDao.updateChapter(chapter)

        // If total lectures changed, adjust lecture rows
        if (old != null && old.totalLectures != chapter.totalLectures) {
            val currentLectures = lectureDao.getLecturesForChapterSync(chapter.id)
            if (chapter.totalLectures > currentLectures.size) {
                val newOnes = ((currentLectures.size + 1)..chapter.totalLectures).map { lNum ->
                    LectureEntity(
                        chapterId = chapter.id,
                        lectureNumber = lNum,
                        title = "Lecture $lNum: ${chapter.name.take(24)}"
                    )
                }
                lectureDao.insertLectures(newOnes)
            } else if (chapter.totalLectures < currentLectures.size) {
                val toRemove = currentLectures.filter { it.lectureNumber > chapter.totalLectures }
                toRemove.forEach { lectureDao.deleteLecture(it) }
            }
        }
    }

    suspend fun toggleChapterHidden(chapterId: Long, isHidden: Boolean) {
        chapterDao.setChapterHidden(chapterId, isHidden)
    }

    suspend fun toggleChapterImportant(chapterId: Long) {
        chapterDao.toggleChapterImportant(chapterId)
    }

    suspend fun toggleChapterExcluded(chapterId: Long, excluded: Boolean) {
        chapterDao.setChapterExcluded(chapterId, excluded)
    }

    suspend fun deleteChapter(chapter: ChapterEntity) {
        lectureDao.deleteLecturesForChapter(chapter.id)
        chapterDao.deleteChapter(chapter)
    }

    // Lecture Tracking
    suspend fun setLectureCompleted(lectureId: Long, completed: Boolean) {
        val timestamp = if (completed) System.currentTimeMillis() else 0L
        lectureDao.setLectureCompleted(lectureId, completed, timestamp)

        if (completed) {
            recordStudyActivity()
        }
    }

    suspend fun toggleLectureImportant(lectureId: Long) {
        lectureDao.toggleLectureImportant(lectureId)
    }

    suspend fun setLectureNotesCompleted(lectureId: Long, completed: Boolean) {
        lectureDao.setNotesCompleted(lectureId, completed)
    }

    suspend fun setLecturePracticeCompleted(lectureId: Long, completed: Boolean) {
        lectureDao.setPracticeCompleted(lectureId, completed)
    }

    suspend fun updateLectureNote(lectureId: Long, note: String) {
        lectureDao.updatePersonalNote(lectureId, note)
    }

    // Study Plan Operations
    suspend fun saveStudyPlan(plan: StudyPlanEntity) {
        studyPlanDao.insertPlan(plan)
    }

    suspend fun updateStudyPlan(plan: StudyPlanEntity) {
        val existing = studyPlanDao.getPlanForExamSync(plan.examId)
        val toSave = if (existing != null) {
            plan.copy(id = existing.id)
        } else {
            plan
        }
        studyPlanDao.insertPlan(toSave)
    }

    suspend fun updateWeeklyTimetable(planId: Long, entries: List<WeeklyTimetableEntryEntity>) {
        weeklyTimetableDao.deleteTimetableForPlan(planId)
        weeklyTimetableDao.insertEntries(entries)
    }

    suspend fun applyMissedWorkloadAdjustment(planId: Long, missedHours: Float, mode: String) {
        val plan = studyPlanDao.getPlanForExamSync(planId) ?: return
        when (mode) {
            "DISTRIBUTE" -> {
                // Add missed workload to adjustment which spreads it across remaining days
                val updated = plan.copy(
                    missedWorkloadHoursAdjustment = plan.missedWorkloadHoursAdjustment + missedHours
                )
                studyPlanDao.updatePlan(updated)
            }
            "EXTEND_DEADLINE" -> {
                // Calculate how many extra days are needed for missedHours based on current pace
                val extraDays = (missedHours / 3.0f).toInt().coerceAtLeast(1)
                val cal = Calendar.getInstance()
                cal.timeInMillis = if (plan.targetDateTimestamp > 0) plan.targetDateTimestamp else System.currentTimeMillis()
                cal.add(Calendar.DAY_OF_YEAR, extraDays)
                val updated = plan.copy(
                    targetDays = plan.targetDays + extraDays,
                    targetDateTimestamp = cal.timeInMillis
                )
                studyPlanDao.updatePlan(updated)
            }
            "CLEAR" -> {
                val updated = plan.copy(missedWorkloadHoursAdjustment = 0f)
                studyPlanDao.updatePlan(updated)
            }
        }
    }

    // ==========================================
    // FOCUS MODE ARCHITECTURE (Settings, Allowed Apps, Sessions)
    // ==========================================

    // 1. Focus Settings
    fun getFocusSettings(): Flow<FocusSettingsEntity?> = focusSettingsDao.getSettings()
    suspend fun getFocusSettingsSync(): FocusSettingsEntity? = focusSettingsDao.getSettingsSync()

    suspend fun ensureFocusSettingsInitialized(): FocusSettingsEntity {
        val existing = focusSettingsDao.getSettingsSync()
        if (existing != null) return existing
        val defaultSettings = FocusSettingsEntity(id = 1)
        focusSettingsDao.insertOrUpdate(defaultSettings)
        return defaultSettings
    }

    suspend fun saveFocusSettings(settings: FocusSettingsEntity) {
        focusSettingsDao.insertOrUpdate(
            settings.copy(
                id = 1,
                lastUpdatedTimestamp = System.currentTimeMillis()
            )
        )
    }

    suspend fun updateFocusTimerState(
        isActive: Boolean,
        isPaused: Boolean,
        remainingSeconds: Int,
        targetEndTime: Long?
    ) {
        val current = focusSettingsDao.getSettingsSync() ?: FocusSettingsEntity(id = 1)
        focusSettingsDao.insertOrUpdate(
            current.copy(
                isFocusModeActive = isActive,
                isPaused = isPaused,
                remainingSeconds = remainingSeconds,
                targetEndTimeTimestamp = targetEndTime,
                lastUpdatedTimestamp = System.currentTimeMillis()
            )
        )
    }

    // 2. Focus Allowed Apps
    fun getAllAllowedApps(): Flow<List<FocusAllowedAppEntity>> = focusAllowedAppDao.getAllAllowedApps()
    suspend fun getAllAllowedAppsSync(): List<FocusAllowedAppEntity> = focusAllowedAppDao.getAllAllowedAppsSync()

    fun getEnabledAllowedApps(): Flow<List<FocusAllowedAppEntity>> = focusAllowedAppDao.getEnabledAllowedApps()
    suspend fun getEnabledAllowedAppsSync(): List<FocusAllowedAppEntity> = focusAllowedAppDao.getEnabledAllowedAppsSync()

    suspend fun setAppAllowed(
        packageName: String,
        appName: String,
        isAllowed: Boolean,
        category: String = "EDUCATION"
    ) {
        focusAllowedAppDao.insertOrUpdate(
            FocusAllowedAppEntity(
                packageName = packageName,
                appName = appName,
                isEnabled = isAllowed,
                category = category,
                updatedAt = System.currentTimeMillis()
            )
        )
    }

    suspend fun toggleAppAllowed(packageName: String, isAllowed: Boolean) {
        focusAllowedAppDao.setAppEnabled(packageName, isAllowed)
    }

    suspend fun removeAllowedApp(packageName: String) {
        focusAllowedAppDao.delete(packageName)
    }

    suspend fun seedDefaultAllowedAppsIfEmpty() {
        // In Phase 4, apps are dynamically discovered from the user's actual device.
        // No hardcoded universal apps are automatically injected.
    }

    // 3. Focus Sessions
    fun getActiveFocusSession(): Flow<FocusSessionEntity?> = focusSessionDao.getActiveSession()
    suspend fun getActiveFocusSessionSync(): FocusSessionEntity? = focusSessionDao.getActiveSessionSync()

    suspend fun startFocusSession(
        goalId: Long?,
        taskId: Long? = null,
        subjectId: Long? = null,
        subjectName: String,
        chapterName: String = "",
        topicName: String = "",
        durationMinutes: Int,
        sessionType: String = "FOCUS"
    ): Long {
        val now = System.currentTimeMillis()
        val session = FocusSessionEntity(
            goalId = goalId,
            taskId = taskId,
            subjectId = subjectId,
            subjectName = subjectName,
            chapterName = chapterName,
            topicName = topicName,
            durationMinutes = durationMinutes,
            sessionType = sessionType,
            startTime = now,
            endTime = now + durationMinutes * 60 * 1000L,
            status = "IN_PROGRESS",
            completedAtTimestamp = 0L,
            createdAt = now
        )
        return focusSessionDao.insertSession(session)
    }

    suspend fun pauseFocusSession(sessionId: Long, activeDurationSeconds: Long, pausedDurationSeconds: Long) {
        val session = focusSessionDao.getSessionById(sessionId) ?: return
        focusSessionDao.updateSession(
            session.copy(
                status = "PAUSED",
                activeDuration = activeDurationSeconds,
                pausedDuration = pausedDurationSeconds
            )
        )
    }

    suspend fun resumeFocusSession(sessionId: Long) {
        val session = focusSessionDao.getSessionById(sessionId) ?: return
        focusSessionDao.updateSession(session.copy(status = "IN_PROGRESS"))
    }

    suspend fun completeFocusSession(
        sessionId: Long,
        activeDurationSeconds: Long,
        pausedDurationSeconds: Long
    ) {
        val now = System.currentTimeMillis()
        val session = focusSessionDao.getSessionById(sessionId)
        if (session != null) {
            val minutes = (activeDurationSeconds / 60).coerceAtLeast(1).toInt()
            focusSessionDao.updateSession(
                session.copy(
                    status = "COMPLETED",
                    endTime = now,
                    activeDuration = activeDurationSeconds,
                    pausedDuration = pausedDurationSeconds,
                    completedAtTimestamp = now,
                    durationMinutes = minutes
                )
            )
            if (session.sessionType == "FOCUS") {
                recordStudyActivity(minutes.toLong())
            }
        }
    }

    suspend fun abandonFocusSession(
        sessionId: Long,
        activeDurationSeconds: Long,
        pausedDurationSeconds: Long
    ) {
        val now = System.currentTimeMillis()
        val session = focusSessionDao.getSessionById(sessionId)
        if (session != null) {
            focusSessionDao.updateSession(
                session.copy(
                    status = "ABANDONED",
                    endTime = now,
                    activeDuration = activeDurationSeconds,
                    pausedDuration = pausedDurationSeconds,
                    completedAtTimestamp = now
                )
            )
        }
    }

    // Existing Focus Sessions compatibility
    suspend fun logFocusSession(
        subjectName: String,
        chapterName: String,
        topicName: String,
        durationMinutes: Int,
        sessionType: String = "FOCUS"
    ): Long {
        val session = FocusSessionEntity(
            subjectName = subjectName,
            chapterName = chapterName,
            topicName = topicName,
            durationMinutes = durationMinutes,
            sessionType = sessionType,
            completedAtTimestamp = System.currentTimeMillis()
        )
        val id = focusSessionDao.insertSession(session)
        recordStudyActivity(durationMinutes.toLong())
        return id
    }

    // Streak Logic
    private suspend fun recordStudyActivity(addedFocusMinutes: Long = 0L) {
        val todayStr = getTodayDateString()
        val currentStreakEntity = userStreakDao.getStreakSync() ?: UserStreakEntity()

        val lastDate = currentStreakEntity.lastStudyDateString
        var newCurrentStreak = currentStreakEntity.currentStreak
        var newLongestStreak = currentStreakEntity.longestStreak

        if (lastDate.isEmpty()) {
            newCurrentStreak = 1
        } else if (lastDate == todayStr) {
            // Already counted today
        } else {
            val yesterdayStr = getYesterdayDateString()
            if (lastDate == yesterdayStr) {
                newCurrentStreak += 1
            } else {
                newCurrentStreak = 1 // Reset streak if missed more than 1 day
            }
        }

        if (newCurrentStreak > newLongestStreak) {
            newLongestStreak = newCurrentStreak
        }

        val updated = currentStreakEntity.copy(
            currentStreak = newCurrentStreak,
            longestStreak = newLongestStreak,
            lastStudyDateString = todayStr,
            totalFocusMinutes = currentStreakEntity.totalFocusMinutes + addedFocusMinutes,
            totalLecturesCompleted = currentStreakEntity.totalLecturesCompleted + (if (addedFocusMinutes == 0L) 1 else 0)
        )
        userStreakDao.insertOrUpdate(updated)
    }

    private fun getTodayDateString(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }

    private fun getYesterdayDateString(): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    }

    suspend fun resetAndReloadSyllabusForActiveGoal(): Boolean {
        val goal = examGoalDao.getActiveGoalSync() ?: return false
        val plan = studyPlanDao.getPlanForExamSync(goal.id)
        val targetDays = plan?.targetDays ?: 90
        val targetTimestamp = plan?.targetDateTimestamp

        // Re-initialize using clean template
        initializeExamGoalWithDefaultSyllabus(
            category = goal.category,
            examName = goal.examName,
            year = goal.targetYear,
            targetDays = targetDays,
            customTargetTimestamp = targetTimestamp
        )
        return true
    }

    suspend fun resetAllProgressForActiveGoal(): Boolean {
        val goal = examGoalDao.getActiveGoalSync() ?: return false
        val chapters = chapterDao.getAllChaptersForExamSync(goal.id)
        val chIds = chapters.map { it.id }.toSet()
        val lectures = lectureDao.getAllLecturesForExamSync(goal.id)
        val resetLecs = lectures.filter { it.chapterId in chIds }.map {
            it.copy(
                isCompleted = false,
                notesCompleted = false,
                practiceCompleted = false,
                completedAtTimestamp = 0L
            )
        }
        lectureDao.insertLectures(resetLecs)
        return true
    }

    suspend fun switchActiveGoal(goalId: Long) {
        examGoalDao.deactivateAllGoals()
        examGoalDao.activateGoal(goalId)
    }
}
