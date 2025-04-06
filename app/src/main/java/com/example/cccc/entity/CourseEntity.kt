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
    val category: String,
    val rating: Double,
    val instructor: String,
    val duration: String,
    val isPurchased: Boolean = false,
    val createdAt: Date = Date(),
    val updatedAt: Date = Date()
) 