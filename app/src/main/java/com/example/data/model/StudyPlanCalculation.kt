package com.example.data.model

import java.util.Locale

data class StudyPlanCalculation(
    val totalLectures: Int,
    val completedLectures: Int,
    val remainingLectures: Int,

    // Metric A: FIXED 2-HOUR REFERENCE
    val totalReferenceHours: Double,
    val completedReferenceHours: Double,
    val remainingReferenceHours: Double,

    // Metric B: ESTIMATED WATCH TIME (Affected by playback speed)
    val playbackSpeed: Float,
    val totalEstimatedWatchHours: Double,
    val completedEstimatedWatchHours: Double,
    val remainingEstimatedWatchHours: Double,

    // Notes Workload
    val notesMinutesPerLecture: Int,
    val totalNotesHours: Double,
    val completedNotesHours: Double,
    val remainingNotesHours: Double,

    // Practice / Revision Workload
    val dailyPracticeMinutes: Int,
    val dailyRevisionMinutes: Int,
    val dailyPracticeHours: Double,
    val dailyRevisionHours: Double,

    // Total Workloads
    val targetDays: Int,
    val totalEstimatedWorkloadHours: Double,
    val remainingEstimatedWorkloadHours: Double,
    val dailyEstimatedWorkloadHours: Double,

    // Formatted strings
    val dailyWorkloadFormatted: String,
    val totalWorkloadFormatted: String,
    val referenceHoursFormatted: String,
    val estimatedWatchHoursFormatted: String
) {
    companion object {
        const val FIXED_LECTURE_REFERENCE_HOURS = 2.0 // Fixed 2 hours per lecture

        fun calculate(
            totalLectures: Int,
            completedLectures: Int,
            playbackSpeed: Float,
            notesMinutesPerLecture: Int,
            dailyPracticeMinutes: Int,
            dailyRevisionMinutes: Int,
            targetDays: Int,
            missedAdjustmentHours: Float = 0f
        ): StudyPlanCalculation {
            val safeTotal = totalLectures.coerceAtLeast(0)
            val safeCompleted = completedLectures.coerceIn(0, safeTotal)
            val remainingLectures = (safeTotal - safeCompleted).coerceAtLeast(0)
            val safeDays = targetDays.coerceAtLeast(1)
            val safeSpeed = if (playbackSpeed > 0f) playbackSpeed else 1.0f

            // A. Fixed 2-Hour Reference
            val totalReferenceHours = safeTotal * FIXED_LECTURE_REFERENCE_HOURS
            val completedReferenceHours = safeCompleted * FIXED_LECTURE_REFERENCE_HOURS
            val remainingReferenceHours = remainingLectures * FIXED_LECTURE_REFERENCE_HOURS

            // B. Estimated Watch Time
            val totalEstimatedWatchHours = totalReferenceHours / safeSpeed
            val completedEstimatedWatchHours = completedReferenceHours / safeSpeed
            val remainingEstimatedWatchHours = remainingReferenceHours / safeSpeed

            // Notes Workload
            val totalNotesHours = (safeTotal * notesMinutesPerLecture) / 60.0
            val completedNotesHours = (safeCompleted * notesMinutesPerLecture) / 60.0
            val remainingNotesHours = (remainingLectures * notesMinutesPerLecture) / 60.0

            // Practice & Revision
            val dailyPracticeHours = dailyPracticeMinutes / 60.0
            val dailyRevisionHours = dailyRevisionMinutes / 60.0
            val totalPracticeHours = dailyPracticeHours * safeDays
            val totalRevisionHours = dailyRevisionHours * safeDays

            // Total Workload
            val totalWorkload = totalEstimatedWatchHours + totalNotesHours + totalPracticeHours + totalRevisionHours
            val remainingWatchAndNotes = remainingEstimatedWatchHours + remainingNotesHours + missedAdjustmentHours.toDouble()
            val dailyWorkload = (remainingWatchAndNotes / safeDays) + dailyPracticeHours + dailyRevisionHours

            return StudyPlanCalculation(
                totalLectures = safeTotal,
                completedLectures = safeCompleted,
                remainingLectures = remainingLectures,
                totalReferenceHours = totalReferenceHours,
                completedReferenceHours = completedReferenceHours,
                remainingReferenceHours = remainingReferenceHours,
                playbackSpeed = safeSpeed,
                totalEstimatedWatchHours = totalEstimatedWatchHours,
                completedEstimatedWatchHours = completedEstimatedWatchHours,
                remainingEstimatedWatchHours = remainingEstimatedWatchHours,
                notesMinutesPerLecture = notesMinutesPerLecture,
                totalNotesHours = totalNotesHours,
                completedNotesHours = completedNotesHours,
                remainingNotesHours = remainingNotesHours,
                dailyPracticeMinutes = dailyPracticeMinutes,
                dailyRevisionMinutes = dailyRevisionMinutes,
                dailyPracticeHours = dailyPracticeHours,
                dailyRevisionHours = dailyRevisionHours,
                targetDays = safeDays,
                totalEstimatedWorkloadHours = totalWorkload,
                remainingEstimatedWorkloadHours = remainingWatchAndNotes + (dailyPracticeHours + dailyRevisionHours) * safeDays,
                dailyEstimatedWorkloadHours = dailyWorkload,
                dailyWorkloadFormatted = formatHoursMinutes(dailyWorkload),
                totalWorkloadFormatted = formatHoursMinutes(totalWorkload),
                referenceHoursFormatted = formatHoursMinutes(totalReferenceHours),
                estimatedWatchHoursFormatted = formatHoursMinutes(totalEstimatedWatchHours)
            )
        }

        fun formatHoursMinutes(hoursDecimal: Double): String {
            val totalMinutes = (hoursDecimal * 60).toLong().coerceAtLeast(0)
            val h = totalMinutes / 60
            val m = totalMinutes % 60
            return when {
                h > 0 && m > 0 -> "${h}h ${m}m"
                h > 0 -> "${h}h"
                else -> "${m}m"
            }
        }
    }
}
