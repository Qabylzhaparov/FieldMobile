package com.example.cccc.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.cccc.db.CourseDao
import com.example.cccc.db.CourseDatabase
import com.example.cccc.vm.CourseRepository
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val application: Application) {

    @Provides
    fun provideApplication(): Application = application

    @Provides
    fun provideContext(): Context = application.applicationContext

    @Provides
    fun provideCourseDao(courseDatabase: CourseDatabase): CourseDao {
        return courseDatabase.CourseDao()
    }

    @Provides
    fun provideCourseDatabase(context: Context): CourseDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CourseDatabase::class.java,
            "Course_database"
        ).build()
    }

    @Provides
    fun provideCourseRepository(courseDao: CourseDao): CourseRepository {
        return CourseRepository(courseDao)
    }
}
