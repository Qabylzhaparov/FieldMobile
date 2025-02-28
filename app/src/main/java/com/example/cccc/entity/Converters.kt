package com.example.cccc.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromVideoList(value: List<Video>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toVideoList(value: String): List<Video> {
        val listType = object : TypeToken<List<Video>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromTestList(value: List<Test>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTestList(value: String): List<Test> {
        val listType = object : TypeToken<List<Test>>() {}.type
        return gson.fromJson(value, listType)
    }
}
