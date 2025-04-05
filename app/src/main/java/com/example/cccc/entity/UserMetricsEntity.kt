package com.example.cccc.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cccc.util.DateConverter
import java.util.Date

@Entity(tableName = "user_metrics")
@TypeConverters(DateConverter::class)
data class UserMetricsEntity(
    @PrimaryKey
    val userId: String,
    val coursesCompleted: Int = 0,
    val testsPassed: Int = 0,
    val totalHoursWatched: Int = 0,
    val certificatesEarned: Int = 0,
    val lastSynced: Date = Date()
) 