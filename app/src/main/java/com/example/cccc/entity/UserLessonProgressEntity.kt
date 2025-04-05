package com.example.cccc.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.util.DateConverter
import java.util.Date

@Entity(
    tableName = "user_lesson_progress",
    foreignKeys = [
        ForeignKey(
            entity = UserCourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["userCourseId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LessonEntity::class,
            parentColumns = ["id"],
            childColumns = ["lessonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["userCourseId", "lessonId"], unique = true),
        Index(value = ["lessonId"])
    ]
)
@TypeConverters(DateConverter::class)
data class UserLessonProgressEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val userCourseId: Long,
    val lessonId: String,
    val isCompleted: Boolean = false,
    val videosWatched: Int = 0,
    val testsPassed: Int = 0,
    val lastAccessed: Date = Date(),
    val lastSynced: Date = Date()
) 