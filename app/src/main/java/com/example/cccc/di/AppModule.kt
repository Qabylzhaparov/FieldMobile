package com.example.cccc.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.cccc.dao.CourseDao
import com.example.cccc.database.AppDatabase
import com.example.cccc.database.CourseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideCourseDao(courseDatabase: AppDatabase): CourseDao {
        return courseDatabase.courseDao()
    }


    @Provides
    fun provideCourseDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCourseRepository(courseDao: CourseDao): CourseRepository {
        return CourseRepository(courseDao)
    }
}
