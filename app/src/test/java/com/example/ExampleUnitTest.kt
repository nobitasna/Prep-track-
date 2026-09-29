package com.example

import com.example.data.model.StudyPlanCalculation
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testPromptSection31SampleCalculation() {
    // 60 lectures, 0 completed, 1.5x playback, 30min notes, 60min practice daily, 0 revision, 34 days
    // In prompt:
    // Reference: 60 * 2h = 120h
    // Watch: 120 / 1.5 = 80h
    // Notes: 60 * 0.5 = 30h
    // Practice: 60 hours total across 34 days -> ~105.8 min/day or daily workload formula:
    // Let's verify our StudyPlanCalculation formulas:
    val calc = StudyPlanCalculation.calculate(
      totalLectures = 60,
      completedLectures = 0,
      playbackSpeed = 1.5f,
      notesMinutesPerLecture = 30,
      dailyPracticeMinutes = 60,
      dailyRevisionMinutes = 0,
      targetDays = 34
    )

    // Reference hours MUST be exactly 120 hours
    assertEquals(120.0, calc.totalReferenceHours, 0.001)

    // Estimated watch hours MUST be exactly 80 hours
    assertEquals(80.0, calc.totalEstimatedWatchHours, 0.001)

    // Notes workload MUST be 30 hours
    assertEquals(30.0, calc.totalNotesHours, 0.001)

    // Reference remains 120 hours regardless of playback speed!
    assertEquals("120h", calc.referenceHoursFormatted)
    assertEquals("80h", calc.estimatedWatchHoursFormatted)
  }
}
