package com.example.cccc.firetest

import android.util.Log
import com.example.cccc.entity.Test
import com.example.cccc.entity.Video
import com.google.firebase.firestore.FirebaseFirestore

object FireTestParser {

    fun parseLessons(doc: Map<String, Any>): List<LessonRemote> {
        val lessonsRaw = doc["lessons"] as? List<Map<String, Any>> ?: return emptyList()

        return lessonsRaw.map { lessonMap ->
            val videoMap = lessonMap["video"] as? Map<String, Any>
            val testMap = lessonMap["test"] as? Map<String, Any>

            LessonRemote(
                id = lessonMap["id"] as String,
                title = lessonMap["title"] as String,
                description = lessonMap["description"] as String,
                order = (lessonMap["order"] as Long).toInt(),
                duration = (lessonMap["duration"] as Long).toInt(),
                isLocked = lessonMap["isLocked"] as Boolean,
                isCompleted = lessonMap["isCompleted"] as Boolean,
                video = videoMap?.let {
                    Video(
                        id = (it["id"] as Long).toInt(),
                        course = (it["course"] as Long).toInt(),
                        title = it["title"] as String,
                        url = it["url"] as String
                    )
                },
                test = testMap?.let {
                    Test(
                        id = (it["id"] as Long).toInt(),
                        question = it["question"] as String,
                        options = it["options"] as List<String>,
                        correctAnswer = (it["correctAnswer"] as Long).toInt()
                    )
                }
            )
        }
    }
}

