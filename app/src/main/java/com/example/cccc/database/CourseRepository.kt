package com.example.cccc.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.cccc.dao.CourseDao
import com.example.cccc.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {

    fun getAllCourses(): LiveData<List<CourseEntity>> {
        return courseDao.getAllCourses().asLiveData()
    }

}
