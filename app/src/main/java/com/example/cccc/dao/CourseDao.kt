package com.example.cccc.dao

import androidx.room.*
import com.example.cccc.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    suspend fun getCourseById(courseId: String): CourseEntity?

    @Query("SELECT * FROM courses WHERE isPurchased = 1")
    fun getPurchasedCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    @Query("SELECT * FROM courses WHERE lastSynced < :timestamp")
    suspend fun getUnsyncedCourses(timestamp: Long): List<CourseEntity>

    @Query("UPDATE courses SET isPurchased = :isPurchased WHERE id = :courseId")
    suspend fun updatePurchaseStatus(courseId: String, isPurchased: Boolean)
} 