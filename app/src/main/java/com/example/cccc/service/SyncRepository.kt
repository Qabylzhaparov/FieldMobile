//package com.example.cccc.service
//
//import com.example.cccc.database.AppDatabase
//import com.example.cccc.entity.*
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.withContext
//import java.util.Date
//
//class SyncRepository(
//    private val db: AppDatabase,
//    private val firestore: FirebaseFirestore
//) {
//    private val courseDao = db.courseDao()
//    private val lessonDao = db.lessonDao()
//    private val testDao = db.testDao()
////    private val userMetricsDao = db.userMetricsDao()
//
//    suspend fun syncCourses() = withContext(Dispatchers.IO) {
//        try {
//            // Получаем курсы из Firestore
//            val coursesSnapshot = firestore.collection("courses").get().await()
//            val firestoreCourses = coursesSnapshot.toObjects(CourseEntity::class.java)
//
//            // Получаем локальные курсы
//            val localCourses = courseDao.getAllCourses()
//
//            // Синхронизируем
//            for (course in firestoreCourses) {
//                courseDao.insertCourse(course.copy(lastSynced = Date()))
//            }
//
//            // Отправляем локальные изменения в Firestore
//            val unsyncedCourses = courseDao.getUnsyncedCourses(Date().time - 3600000) // 1 час
//            for (course in unsyncedCourses) {
//                firestore.collection("courses").document(course.id).set(course).await()
//                courseDao.updateCourse(course.copy(lastSynced = Date()))
//            }
//        } catch (e: Exception) {
//            // Обработка ошибок
//        }
//    }
//
//    suspend fun syncLessons(courseId: String) = withContext(Dispatchers.IO) {
//        try {
//            val lessonsSnapshot = firestore.collection("courses")
//                .document(courseId)
//                .collection("lessons")
//                .get()
//                .await()
//
//            val firestoreLessons = lessonsSnapshot.toObjects(LessonEntity::class.java)
//
//            for (lesson in firestoreLessons) {
//                lessonDao.insertLesson(lesson.copy(lastSynced = Date()))
//            }
//
//            val unsyncedLessons = lessonDao.getUnsyncedLessons(Date().time - 3600000)
//            for (lesson in unsyncedLessons) {
//                firestore.collection("courses")
//                    .document(lesson.courseId)
//                    .collection("lessons")
//                    .document(lesson.id)
//                    .set(lesson)
//                    .await()
//                lessonDao.updateLesson(lesson.copy(lastSynced = Date()))
//            }
//        } catch (e: Exception) {
//            // Обработка ошибок
//        }
//    }
//
//    suspend fun syncTests(courseId: String) = withContext(Dispatchers.IO) {
//        try {
//            val testsSnapshot = firestore.collection("courses")
//                .document(courseId)
//                .collection("tests")
//                .get()
//                .await()
//
//            val firestoreTests = testsSnapshot.toObjects(TestEntity::class.java)
//
//            for (test in firestoreTests) {
//                testDao.insertTest(test.copy(lastSynced = Date()))
//            }
//
//            val unsyncedTests = testDao.getUnsyncedTests(Date().time - 3600000)
//            for (test in unsyncedTests) {
//                firestore.collection("courses")
//                    .document(test.courseId)
//                    .collection("tests")
//                    .document(test.id)
//                    .set(test)
//                    .await()
//                testDao.updateTest(test.copy(lastSynced = Date()))
//            }
//        } catch (e: Exception) {
//            // Обработка ошибок
//        }
//    }
//
//    suspend fun syncUserMetrics(userId: String) = withContext(Dispatchers.IO) {
//        try {
//            val metricsDoc = firestore.collection("users")
//                .document(userId)
//                .collection("metrics")
//                .document("user_metrics")
//                .get()
//                .await()
//
//            val firestoreMetrics = metricsDoc.toObject(UserMetricsEntity::class.java)
//            if (firestoreMetrics != null) {
//                userMetricsDao.insertUserMetrics(firestoreMetrics.copy(lastSynced = Date()))
//            }
//
//            val unsyncedMetrics = userMetricsDao.getUnsyncedMetrics(Date().time - 3600000)
//            for (metrics in unsyncedMetrics) {
//                firestore.collection("users")
//                    .document(userId)
//                    .collection("metrics")
//                    .document("user_metrics")
//                    .set(metrics)
//                    .await()
//                userMetricsDao.updateUserMetrics(metrics.copy(lastSynced = Date()))
//            }
//        } catch (e: Exception) {
//            // Обработка ошибок
//        }
//    }
//
//    fun observeUserMetrics(userId: String): Flow<UserMetricsEntity?> {
//        return userMetricsDao.getUserMetrics(userId)
//    }
//}