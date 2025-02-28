package com.example.cccc.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.parcelize.Parcelize

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
    val category: String,
    val videos: List<Video>,
    val tests: List<Test>
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
