package com.example.cccc.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.util.DateConverter
import java.util.Date

@Entity(tableName = "courses")
@TypeConverters(DateConverter::class)
data class CourseEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val duration: Int,
    val category: String,
    val totalLessons: Int = 0,
    val totalVideos: Int = 0,
    val totalTests: Int = 0,
    val isPurchased: Boolean = false,
    val lastSynced: Date = Date()
) 