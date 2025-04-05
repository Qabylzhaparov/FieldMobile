package com.example.cccc.util

import androidx.room.TypeConverter
import com.example.cccc.model.Question
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

class QuestionListConverter {
    @TypeConverter
    fun fromString(value: String): List<Question> {
        val listType = object : TypeToken<List<Question>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Question>): String {
        return Gson().toJson(list)
    }
} 