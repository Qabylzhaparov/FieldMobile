package com.example.cccc.db

import com.example.cccc.entity.Course
import com.example.cccc.entity.Test
import com.example.cccc.entity.Video

object CourseRepository {
    fun getCourses(): List<Course> {
        return listOf(
            Course(
                id = 1,
                name = "Kotlin для начинающих",
                slug = "kotlin-beginners",
                description = "Изучение основ Kotlin с нуля",
                imageUrl = "https://example.com/kotlin.jpg",
                price = 19.99,
                category = "Программирование",
                videos = listOf(
                    Video(id = 1, course = 1, title = "Введение в Kotlin", url = "https://example.com/video1"),
                    Video(id = 2, course = 1, title = "Переменные и типы данных", url = "https://example.com/video2")
                ),
                tests = listOf(
                    Test(id = 1, question = "Что такое Kotlin?", options = listOf("Язык", "Среда", "Фреймворк"), correctAnswer = 0)
                )
            ),
            Course(
                id = 2,
                name = "Основы Android",
                slug = "android-basics",
                description = "Как создавать мобильные приложения на Android",
                imageUrl = "https://example.com/android.jpg",
                price = 29.99,
                category = "Мобильная разработка",
                videos = listOf(
                    Video(id = 3, course = 2, title = "Activity и фрагменты", url = "https://example.com/video3"),
                    Video(id = 4, course = 2, title = "Работа с UI", url = "https://example.com/video4")
                ),
                tests = listOf(
                    Test(id = 2, question = "Что такое Activity?", options = listOf("Класс", "Файл", "Метод"), correctAnswer = 0)
                )
            ),
            Course(
                id = 3,
                name = "Jetpack Compose с нуля",
                slug = "jetpack-compose",
                description = "Современная разработка UI в Android с помощью Jetpack Compose",
                imageUrl = "https://example.com/compose.jpg",
                price = 34.99,
                category = "Мобильная разработка",
                videos = listOf(
                    Video(id = 5, course = 3, title = "Введение в Jetpack Compose", url = "https://example.com/video5"),
                    Video(id = 6, course = 3, title = "Работа с состоянием в Compose", url = "https://example.com/video6")
                ),
                tests = listOf(
                    Test(id = 3, question = "Какой язык используется в Jetpack Compose?", options = listOf("Java", "Kotlin", "Dart"), correctAnswer = 1)
                )
            ),
            Course(
                id = 4,
                name = "Spring Boot для начинающих",
                slug = "spring-boot",
                description = "Разработка серверных приложений с использованием Spring Boot",
                imageUrl = "https://example.com/spring.jpg",
                price = 39.99,
                category = "Backend-разработка",
                videos = listOf(
                    Video(id = 7, course = 4, title = "Основы Spring Boot", url = "https://example.com/video7"),
                    Video(id = 8, course = 4, title = "Работа с базой данных", url = "https://example.com/video8")
                ),
                tests = listOf(
                    Test(id = 4, question = "Какой язык используется в Spring Boot?", options = listOf("C++", "Python", "Java"), correctAnswer = 2)
                )
            ),
            Course(
                id = 5,
                name = "Python для анализа данных",
                slug = "python-data-analysis",
                description = "Как анализировать данные с использованием Pandas и NumPy",
                imageUrl = "https://example.com/python.jpg",
                price = 24.99,
                category = "Анализ данных",
                videos = listOf(
                    Video(id = 9, course = 5, title = "Основы Pandas", url = "https://example.com/video9"),
                    Video(id = 10, course = 5, title = "Работа с массивами в NumPy", url = "https://example.com/video10")
                ),
                tests = listOf(
                    Test(id = 5, question = "Какой модуль используется для работы с таблицами?", options = listOf("Pandas", "NumPy", "Matplotlib"), correctAnswer = 0)
                )
            ),
            Course(
                id = 6,
                name = "Основы машинного обучения",
                slug = "machine-learning",
                description = "Как строить модели машинного обучения с Scikit-learn",
                imageUrl = "https://example.com/ml.jpg",
                price = 49.99,
                category = "Машинное обучение",
                videos = listOf(
                    Video(id = 11, course = 6, title = "Введение в машинное обучение", url = "https://example.com/video11"),
                    Video(id = 12, course = 6, title = "Обучение моделей", url = "https://example.com/video12")
                ),
                tests = listOf(
                    Test(id = 6, question = "Какой метод используется для классификации?", options = listOf("KNN", "SQL", "HTML"), correctAnswer = 0)
                )
            )
        )
    }
}
