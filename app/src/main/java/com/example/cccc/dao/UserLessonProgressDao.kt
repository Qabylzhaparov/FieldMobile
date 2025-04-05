package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.UserLessonProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserLessonProgressDao {
    @Query("SELECT * FROM user_lesson_progress WHERE userCourseId = :userCourseId")
    fun getLessonProgressByUserCourseId(userCourseId: String): Flow<List<UserLessonProgressEntity>>

    @Query("SELECT * FROM user_lesson_progress WHERE userCourseId = :userCourseId AND lessonId = :lessonId")
    suspend fun getLessonProgress(userCourseId: String, lessonId: String): UserLessonProgressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonProgress(progress: UserLessonProgressEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessonProgresses(progresses: List<UserLessonProgressEntity>)

    @Update
    suspend fun updateLessonProgress(progress: UserLessonProgressEntity)

    @Delete
    suspend fun deleteLessonProgress(progress: UserLessonProgressEntity)

    @Query("SELECT * FROM user_lesson_progress WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedProgress(timestamp: Long): List<UserLessonProgressEntity>

    @Query("UPDATE user_lesson_progress SET isCompleted = :isCompleted WHERE userCourseId = :userCourseId AND lessonId = :lessonId")
    suspend fun updateLessonCompletion(userCourseId: String, lessonId: String, isCompleted: Boolean)

    @Query("UPDATE user_lesson_progress SET videosWatched = :videosWatched WHERE userCourseId = :userCourseId AND lessonId = :lessonId")
    suspend fun updateVideosWatched(userCourseId: String, lessonId: String, videosWatched: Int)

    @Query("UPDATE user_lesson_progress SET testsPassed = :testsPassed WHERE userCourseId = :userCourseId AND lessonId = :lessonId")
    suspend fun updateTestsPassed(userCourseId: String, lessonId: String, testsPassed: Int)

    @Query("SELECT COUNT(*) FROM user_lesson_progress WHERE userCourseId = :userCourseId AND isCompleted = 1")
    suspend fun getCompletedLessonsCount(userCourseId: String): Int
} 