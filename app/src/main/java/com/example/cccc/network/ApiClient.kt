package com.example.cccc.network

import com.example.cccc.entity.Course
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.12:8000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CourseService::class.java)
}