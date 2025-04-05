package com.example.cccc.entity

import androidx.privacysandbox.ads.adservices.adid.AdId
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.model.Question
import com.example.cccc.util.DateConverter
import com.example.cccc.util.QuestionListConverter
import java.util.Date

@Entity(
    tableName = "tests",
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
@TypeConverters(DateConverter::class, QuestionListConverter::class)
data class TestEntity(
    @PrimaryKey
    val id: String,
    val courseId: String,
    val lessonId: String,
    val title: String,
    val description: String,
    val questions: List<Question>,
    val passingScore: Int,
    val timeLimit: Int,
    val order: Int,
    val lastSynced: Date = Date()
)
