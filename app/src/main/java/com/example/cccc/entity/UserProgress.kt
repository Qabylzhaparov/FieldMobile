package com.example.cccc.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.util.DateConverter
import java.util.Date

@Entity(tableName = "user_progress")
@TypeConverters(DateConverter::class)
data class UserProgress(
    @PrimaryKey
    val id: String, // Format: "userId:courseId:lessonId"
    val userId: String,
    val courseId: String,
    val lessonId: String,
    val isCompleted: Boolean = false,
    val videosWatched: Int = 0,
    val testsPassed: Int = 0,
    val lastAccessed: Date = Date(),
    val lastSynced: Date = Date()
) 