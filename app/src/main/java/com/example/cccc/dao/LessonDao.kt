package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons WHERE courseId = :courseId ORDER BY `order` ASC")
    fun getLessonsByCourseId(courseId: String): Flow<List<LessonEntity>>

    @Query("SELECT * FROM lessons WHERE id = :lessonId")
    suspend fun getLessonById(lessonId: String): LessonEntity?

    @Query("SELECT * FROM lessons WHERE courseId = :courseId AND `order` = :order")
    suspend fun getLessonByOrder(courseId: String, order: Int): LessonEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLesson(lesson: LessonEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Update
    suspend fun updateLesson(lesson: LessonEntity)

    @Delete
    suspend fun deleteLesson(lesson: LessonEntity)

    @Query("SELECT * FROM lessons WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedLessons(timestamp: Long): List<LessonEntity>

    @Query("UPDATE lessons SET isCompleted = :isCompleted WHERE id = :lessonId")
    suspend fun updateLessonCompletion(lessonId: String, isCompleted: Boolean)

    @Query("UPDATE lessons SET isLocked = :isLocked WHERE id = :lessonId")
    suspend fun updateLessonLockStatus(lessonId: String, isLocked: Boolean)

    @Query("SELECT COUNT(*) FROM lessons WHERE courseId = :courseId")
    suspend fun getLessonCount(courseId: String): Int
} 