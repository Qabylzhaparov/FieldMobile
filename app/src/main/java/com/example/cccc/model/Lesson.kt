package com.example.cccc.model

data class Lesson(
    val id: String,
    val number: Int,
    val title: String,
    val duration: String,
    val isLocked: Boolean = false,
    val videoUrl: String,
    var isCompleted: Boolean = false
)