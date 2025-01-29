package com.example.cccc.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.cccc.Course

@Dao
interface CourseDao {
    @Query("SELECT * FROM Courses")
    fun getAllCourses(): LiveData<List<Course>>

    @Insert
    suspend fun insertCourse(course: Course)

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)
}

