package com.example.cccc.entity

data class Teacher(
    val user: User,
    val bio: String,
    val courses: List<Course>
)
