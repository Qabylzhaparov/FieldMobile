package com.example.cccc

import android.app.Application
import com.example.cccc.di.AppComponent
import com.example.cccc.di.AppModule
import com.example.cccc.di.DaggerAppComponent

class MyApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}
