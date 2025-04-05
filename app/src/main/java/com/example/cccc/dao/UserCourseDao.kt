package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.UserCourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCourseDao {
    @Query("SELECT * FROM user_courses WHERE userId = :userId")
    fun getUserCourses(userId: String): Flow<List<UserCourseEntity>>

    @Query("SELECT * FROM user_courses WHERE userId = :userId AND courseId = :courseId")
    suspend fun getUserCourse(userId: String, courseId: String): UserCourseEntity?

    @Query("SELECT * FROM user_courses WHERE userId = :userId AND isCompleted = 1")
    fun getCompletedUserCourses(userId: String): Flow<List<UserCourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCourse(userCourse: UserCourseEntity): Long

    @Update
    suspend fun updateUserCourse(userCourse: UserCourseEntity)

    @Query("UPDATE user_courses SET progress = :progress WHERE id = :userCourseId")
    suspend fun updateProgress(userCourseId: Long, progress: Int)

    @Query("UPDATE user_courses SET isCompleted = :isCompleted WHERE id = :userCourseId")
    suspend fun updateCompletionStatus(userCourseId: Long, isCompleted: Boolean)

    @Query("SELECT * FROM user_courses WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedUserCourses(timestamp: Long): List<UserCourseEntity>

    @Query("DELETE FROM user_courses WHERE userId = :userId AND courseId = :courseId")
    suspend fun deleteUserCourse(userId: String, courseId: String)
} 