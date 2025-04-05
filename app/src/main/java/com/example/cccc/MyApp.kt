package com.example.cccc

import AppComponent
import android.app.Application
import com.example.cccc.database.AppDatabase
import com.example.cccc.di.AppModule
//import com.example.cccc.di.DaggerAppComponent
//import com.example.cccc.repository.SyncRepository
import com.example.cccc.service.NetworkMonitor
import com.google.firebase.firestore.FirebaseFirestore

class MyApp : Application() {
    private lateinit var database: AppDatabase
    private lateinit var networkMonitor: NetworkMonitor
    lateinit var appComponent: AppComponent
//    private lateinit var syncRepository: SyncRepository

    override fun onCreate() {
        super.onCreate()
        instance = this

        // Инициализация Room
        database = AppDatabase.getDatabase(this)

        // Инициализация Firestore
        val firestore = FirebaseFirestore.getInstance()

        // Инициализация репозитория синхронизации
//        syncRepository = SyncRepository(database, firestore)

        // Инициализация монитора сети
        networkMonitor = NetworkMonitor(this)

        // Инициализация Dagger
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    companion object {
        private lateinit var instance: MyApp

        fun getDatabase(): AppDatabase = instance.database
//        fun getSyncRepository(): SyncRepository = instance.syncRepository
        fun getNetworkMonitor(): NetworkMonitor = instance.networkMonitor
    }
} 