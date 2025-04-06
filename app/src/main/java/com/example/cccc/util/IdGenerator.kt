package com.example.cccc.util

object IdGenerator {
    fun generateId(userId: String, courseId: String): Long {
        val combined = "$userId:$courseId"
        return combined.hashCode().toLong()
    }
} 