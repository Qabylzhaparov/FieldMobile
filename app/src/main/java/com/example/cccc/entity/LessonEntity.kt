package com.example.cccc.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.util.DateConverter
import java.util.Date

@Entity(
    tableName = "lessons",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["courseId"]),
        Index(value = ["courseId", "order"], unique = true)
    ]
)
@TypeConverters(DateConverter::class)
data class LessonEntity(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val title: String,
    val description: String,
    val order: Int,
    val duration: Int,
    val isLocked: Boolean,
    val lastSynced: Date = Date(),
    val isCompleted: Boolean = false
)
