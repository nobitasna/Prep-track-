package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.LectureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LectureDao {
    @Query("SELECT * FROM lectures WHERE chapterId = :chapterId ORDER BY lectureNumber ASC")
    fun getLecturesForChapter(chapterId: Long): Flow<List<LectureEntity>>

    @Query("SELECT * FROM lectures WHERE chapterId = :chapterId ORDER BY lectureNumber ASC")
    suspend fun getLecturesForChapterSync(chapterId: Long): List<LectureEntity>

    @Query("""
        SELECT l.* FROM lectures l
        INNER JOIN chapters c ON l.chapterId = c.id
        INNER JOIN subjects s ON c.subjectId = s.id
        WHERE s.examId = :examId
        ORDER BY s.sortOrder ASC, c.sortOrder ASC, l.lectureNumber ASC
    """)
    fun getAllLecturesForExam(examId: Long): Flow<List<LectureEntity>>

    @Query("""
        SELECT l.* FROM lectures l
        INNER JOIN chapters c ON l.chapterId = c.id
        INNER JOIN subjects s ON c.subjectId = s.id
        WHERE s.examId = :examId
        ORDER BY s.sortOrder ASC, c.sortOrder ASC, l.lectureNumber ASC
    """)
    suspend fun getAllLecturesForExamSync(examId: Long): List<LectureEntity>

    @Query("SELECT * FROM lectures WHERE id = :id LIMIT 1")
    suspend fun getLectureById(id: Long): LectureEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLecture(lecture: LectureEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLectures(lectures: List<LectureEntity>): List<Long>

    @Update
    suspend fun updateLecture(lecture: LectureEntity)

    @Delete
    suspend fun deleteLecture(lecture: LectureEntity)

    @Query("DELETE FROM lectures WHERE chapterId = :chapterId")
    suspend fun deleteLecturesForChapter(chapterId: Long)

    @Query("UPDATE lectures SET isCompleted = :completed, completedAtTimestamp = :timestamp WHERE id = :id")
    suspend fun setLectureCompleted(id: Long, completed: Boolean, timestamp: Long)

    @Query("UPDATE lectures SET isImportant = NOT isImportant WHERE id = :id")
    suspend fun toggleLectureImportant(id: Long)

    @Query("UPDATE lectures SET notesCompleted = :notesCompleted WHERE id = :id")
    suspend fun setNotesCompleted(id: Long, notesCompleted: Boolean)

    @Query("UPDATE lectures SET practiceCompleted = :practiceCompleted WHERE id = :id")
    suspend fun setPracticeCompleted(id: Long, practiceCompleted: Boolean)

    @Query("UPDATE lectures SET personalNote = :note WHERE id = :id")
    suspend fun updatePersonalNote(id: Long, note: String)

    @Query("""
        SELECT COUNT(*) FROM lectures l
        INNER JOIN chapters c ON l.chapterId = c.id
        WHERE c.subjectId = :subjectId AND l.isCompleted = 1
    """)
    fun getCompletedLecturesCountForSubject(subjectId: Long): Flow<Int>

    @Query("""
        SELECT COUNT(*) FROM lectures l
        INNER JOIN chapters c ON l.chapterId = c.id
        WHERE c.subjectId = :subjectId
    """)
    fun getTotalLecturesCountForSubject(subjectId: Long): Flow<Int>
}
