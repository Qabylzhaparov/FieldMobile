package com.example.cccc.firetest

import com.example.cccc.entity.Test
import com.example.cccc.entity.Video

data class LessonRemote(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val order: Int = 0,
    val duration: Int = 0,
    val isLocked: Boolean = false,
    val isCompleted: Boolean = false,
    val video: Video? = null,
    val test: Test? = null
)
