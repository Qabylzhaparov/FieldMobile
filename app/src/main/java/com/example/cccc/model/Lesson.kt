package com.example.cccc.model

data class Lesson(
    val id: Int,
    val number: String,
    val title: String,
    val duration: String,
    val isLocked: Boolean = false
) 