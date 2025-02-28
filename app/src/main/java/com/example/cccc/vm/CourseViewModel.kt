package com.example.cccc.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cccc.entity.Course
import kotlinx.coroutines.launch

class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
    val allCourses: LiveData<List<Course>> = repository.getAllCourses()

    fun insert(course: Course) = viewModelScope.launch {
        repository.insert(course)
    }

    fun update(course: Course) = viewModelScope.launch {
        repository.update(course)
    }

    fun delete(course: Course) = viewModelScope.launch {
        repository.delete(course)
    }
}


