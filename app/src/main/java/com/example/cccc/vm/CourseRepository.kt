package com.example.cccc.vm

import androidx.lifecycle.LiveData
import com.example.cccc.Course
import com.example.cccc.db.CourseDao

class CourseRepository(private val courseDao: CourseDao) {
    fun getAllCourses(): LiveData<List<Course>> {
        return courseDao.getAllCourses()
    }

    suspend fun insert(course: Course) {
        courseDao.insertCourse(course)
    }

    suspend fun update(course: Course) {
        courseDao.updateCourse(course)
    }

    suspend fun delete(course: Course) {
        courseDao.deleteCourse(course)
    }
}


///ds