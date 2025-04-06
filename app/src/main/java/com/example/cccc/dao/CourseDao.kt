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

    @Query("SELECT * FROM courses WHERE category = :category")
    fun getCoursesByCategory(category: String): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: CourseEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(courses: List<CourseEntity>)

    @Update
    suspend fun update(course: CourseEntity)

    @Delete
    suspend fun delete(course: CourseEntity)

//    @Query("SELECT * FROM courses WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
//    fun searchCourses(query: String): Flow<List<CourseEntity>>
//
//    @Query("SELECT * FROM courses ORDER BY rating DESC")
//    fun getPopularCourses(): Flow<List<CourseEntity>>
//
//    @Query("SELECT * FROM courses ORDER BY createdAt DESC")
//    fun getNewCourses(): Flow<List<CourseEntity>>

//    @Query("SELECT * FROM courses WHERE lastSynced < :timestamp")
//    suspend fun getUnsyncedCourses(timestamp: Long): List<CourseEntity>

    @Query("UPDATE courses SET isPurchased = :isPurchased WHERE id = :courseId")
    suspend fun updatePurchaseStatus(courseId: String, isPurchased: Boolean)
} 