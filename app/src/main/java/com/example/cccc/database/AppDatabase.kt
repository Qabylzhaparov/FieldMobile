package com.example.cccc.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cccc.dao.*
import com.example.cccc.entity.*
import com.example.cccc.util.DateConverter
import com.example.cccc.util.QuestionListConverter

@Database(
    entities = [
        CourseEntity::class,
        UserCourseEntity::class,
        LessonEntity::class,
        VideoEntity::class,
        TestEntity::class,
        UserLessonProgressEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class, QuestionListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun userCourseDao(): UserCourseDao
    abstract fun lessonDao(): LessonDao
    abstract fun videoDao(): VideoDao
    abstract fun testDao(): TestDao
    abstract fun userLessonProgressDao(): UserLessonProgressDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 