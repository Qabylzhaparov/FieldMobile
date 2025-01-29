package com.example.cccc.di

import android.app.Application
import com.example.cccc.MainActivity
import com.example.cccc.MyApp
import com.example.cccc.vm.CourseViewModel
import com.example.cccc.vm.CourseViewModelFactory
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(courseViewModel: CourseViewModel)
    fun inject(myApp: MyApp)

    fun courseViewModelFactory(): CourseViewModelFactory
}
