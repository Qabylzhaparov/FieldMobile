package com.example.cccc.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.util.DateConverter
import java.util.Date

@Entity(
    tableName = "user_courses",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userId", "courseId"], unique = true),
        Index(value = ["courseId"])
    ]
)
@TypeConverters(DateConverter::class)
data class UserCourseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userId: String,
    val courseId: String,
    val isCompleted: Boolean = false,
    val progress: Int = 0,
    val lastAccessed: Date = Date(),
    val lastSynced: Date = Date()
) 