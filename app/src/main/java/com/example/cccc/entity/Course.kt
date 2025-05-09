package com.example.cccc.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.model.CourseCategory
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
@Entity(tableName = "Courses")
@TypeConverters(Converters::class)
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val slug: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val category: CourseCategory,
    val videos: List<Video>,
    val tests: List<Test>,
    val rating: Double,
    val isNew: Boolean,
    var isPurchased: Boolean = false,
    val lastSynced: Date = Date()
) : Parcelable

@Parcelize
data class Video(
    val id: Int,
    val course: Int,
    val title: String,
    val url: String
) : Parcelable

@Parcelize
data class Test(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
) : Parcelable
