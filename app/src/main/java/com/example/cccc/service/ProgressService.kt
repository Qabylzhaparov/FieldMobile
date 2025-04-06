package com.example.cccc.service

import com.example.cccc.database.AppDatabase
import com.example.cccc.entity.UserProgress
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.util.Date

class ProgressService(
    private val appDatabase: AppDatabase,
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private val userProgressDao = appDatabase.userProgressDao()

    suspend fun syncProgress() {
        val userId = auth.currentUser?.uid ?: return

        try {
            // Получаем прогресс из Firestore
            val userCourses = firestore.collection("users")
                .document(userId)
                .collection("courses")
                .get()
                .await()

            for (courseDoc in userCourses.documents) {
                val courseId = courseDoc.id
                val courseData = courseDoc.data ?: continue

                // Получаем прогресс уроков
                val lessonProgresses = courseDoc.reference
                    .collection("lessons")
                    .get()
                    .await()

                for (lessonDoc in lessonProgresses.documents) {
                    val lessonId = lessonDoc.id
                    val lessonData = lessonDoc.data ?: continue

                    val progressId = "$userId:$courseId:$lessonId"
                    val progress = UserProgress(
                        id = progressId,
                        userId = userId,
                        courseId = courseId,
                        lessonId = lessonId,
                        isCompleted = lessonData["isCompleted"] as? Boolean ?: false,
                        videosWatched = (lessonData["videosWatched"] as? Number)?.toInt() ?: 0,
                        testsPassed = (lessonData["testsPassed"] as? Number)?.toInt() ?: 0,
                        lastAccessed = (lessonData["lastAccessed"] as? com.google.firebase.Timestamp)?.toDate() ?: Date(),
                        lastSynced = Date()
                    )

                    userProgressDao.insert(progress)
                }
            }

            // Синхронизируем локальные изменения обратно в Firestore
            val unsyncedProgress = userProgressDao.getUnsyncedProgress(Date().time)
            for (progress in unsyncedProgress) {
                val courseRef = firestore.collection("users")
                    .document(userId)
                    .collection("courses")
                    .document(progress.courseId)

                courseRef.collection("lessons")
                    .document(progress.lessonId)
                    .set(mapOf(
                        "isCompleted" to progress.isCompleted,
                        "videosWatched" to progress.videosWatched,
                        "testsPassed" to progress.testsPassed,
                        "lastAccessed" to progress.lastAccessed,
                        "lastSynced" to Date()
                    )).await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun updateLessonProgress(
        userId: String,
        courseId: String,
        lessonId: String,
        isCompleted: Boolean,
        videosWatched: Int = 0,
        testsPassed: Int = 0
    ) {
        try {
            val progressId = "$userId:$courseId:$lessonId"
            val progress = UserProgress(
                id = progressId,
                userId = userId,
                courseId = courseId,
                lessonId = lessonId,
                isCompleted = isCompleted,
                videosWatched = videosWatched,
                testsPassed = testsPassed,
                lastAccessed = Date(),
                lastSynced = Date()
            )

            userProgressDao.insert(progress)

            // Обновляем Firestore
            val courseRef = firestore.collection("users")
                .document(userId)
                .collection("courses")
                .document(courseId)

            courseRef.collection("lessons")
                .document(lessonId)
                .set(mapOf(
                    "isCompleted" to isCompleted,
                    "videosWatched" to videosWatched,
                    "testsPassed" to testsPassed,
                    "lastAccessed" to Date(),
                    "lastSynced" to Date()
                )).await()

            // Обновляем метрики пользователя
            if (isCompleted) {
                val userRef = firestore.collection("users").document(userId)
                userRef.update(
                    "completedLessons", com.google.firebase.firestore.FieldValue.increment(1),
                    "lastActivity", Date()
                ).await()
            }
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getCourseProgress(userId: String, courseId: String) =
        userProgressDao.getCourseProgress(userId, courseId)

    suspend fun getLessonProgress(userId: String, courseId: String, lessonId: String) =
        userProgressDao.getLessonProgress(userId, courseId, lessonId)
} 