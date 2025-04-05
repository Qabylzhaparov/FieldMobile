package com.example.cccc.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.util.DateConverter
import java.util.Date

@Entity(
    tableName = "videos",
    foreignKeys = [
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["lessonId"]),
        Index(value = ["lessonId", "order"], unique = true)
    ]
)
@TypeConverters(DateConverter::class)
data class VideoEntity(
    @PrimaryKey
    val id: String,
    val lessonId: String,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: Int,
    val order: Int,
    val lastSynced: Date = Date()
) 