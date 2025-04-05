package com.example.cccc.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cccc.database.CourseRepository
import com.example.cccc.entity.Course
import com.example.cccc.entity.CourseEntity
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
    val allCourses: LiveData<List<CourseEntity>> = repository.getAllCourses()
}


