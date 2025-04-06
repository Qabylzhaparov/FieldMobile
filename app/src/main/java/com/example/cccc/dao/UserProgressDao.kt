package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.UserProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProgressDao {
    @Query("SELECT * FROM user_progress WHERE userId = :userId AND courseId = :courseId")
    fun getCourseProgress(userId: String, courseId: String): Flow<List<UserProgress>>

    @Query("SELECT * FROM user_progress WHERE userId = :userId AND courseId = :courseId AND lessonId = :lessonId")
    suspend fun getLessonProgress(userId: String, courseId: String, lessonId: String): UserProgress?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(progress: UserProgress)

    @Update
    suspend fun update(progress: UserProgress)

    @Query("SELECT * FROM user_progress WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedProgress(timestamp: Long): List<UserProgress>

    @Query("UPDATE user_progress SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateCompletionStatus(id: String, isCompleted: Boolean)

    @Query("UPDATE user_progress SET videosWatched = :videosWatched WHERE id = :id")
    suspend fun updateVideosWatched(id: String, videosWatched: Int)

    @Query("UPDATE user_progress SET testsPassed = :testsPassed WHERE id = :id")
    suspend fun updateTestsPassed(id: String, testsPassed: Int)

    @Query("SELECT COUNT(*) FROM user_progress WHERE userId = :userId AND courseId = :courseId AND isCompleted = 1")
    suspend fun getCompletedLessonsCount(userId: String, courseId: String): Int

    @Query("SELECT COUNT(*) FROM user_progress WHERE userId = :userId AND courseId = :courseId")
    suspend fun getTotalLessonsCount(userId: String, courseId: String): Int
} 