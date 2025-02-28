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
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin_Icon.png",
                price = 19.99,
                category = "Программирование",
                videos = listOf(
                    Video(id = 1, course = 1, title = "Введение в Kotlin", url = "https://coursevideo.com/kotlin/1"),
                    Video(id = 2, course = 1, title = "Переменные и типы данных", url = "https://coursevideo.com/kotlin/2")
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
                imageUrl = "https://developer.android.com/static/images/brand/Android_Robot.png",
                price = 29.99,
                category = "Мобильная разработка",
                videos = listOf(
                    Video(id = 3, course = 2, title = "Activity и фрагменты", url = "https://coursevideo.com/android/1"),
                    Video(id = 4, course = 2, title = "Работа с UI", url = "https://coursevideo.com/android/2")
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
                imageUrl = "https://developer.android.com/images/brand/compose/compose-logo.svg",
                price = 34.99,
                category = "Мобильная разработка",
                videos = listOf(
                    Video(id = 5, course = 3, title = "Введение в Jetpack Compose", url = "https://coursevideo.com/compose/1"),
                    Video(id = 6, course = 3, title = "Работа с состоянием в Compose", url = "https://coursevideo.com/compose/2")
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
                imageUrl = "https://spring.io/img/spring.svg",
                price = 39.99,
                category = "Backend-разработка",
                videos = listOf(
                    Video(id = 7, course = 4, title = "Основы Spring Boot", url = "https://coursevideo.com/spring/1"),
                    Video(id = 8, course = 4, title = "Работа с базой данных", url = "https://coursevideo.com/spring/2")
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
                imageUrl = "https://www.python.org/static/community_logos/python-logo-generic.svg",
                price = 24.99,
                category = "Анализ данных",
                videos = listOf(
                    Video(id = 9, course = 5, title = "Основы Pandas", url = "https://coursevideo.com/python/1"),
                    Video(id = 10, course = 5, title = "Работа с массивами в NumPy", url = "https://coursevideo.com/python/2")
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
                imageUrl = "https://scikit-learn.org/stable/_static/scikit-learn-logo-small.png",
                price = 49.99,
                category = "Машинное обучение",
                videos = listOf(
                    Video(id = 11, course = 6, title = "Введение в машинное обучение", url = "https://coursevideo.com/ml/1"),
                    Video(id = 12, course = 6, title = "Обучение моделей", url = "https://coursevideo.com/ml/2")
                ),
                tests = listOf(
                    Test(id = 6, question = "Какой метод используется для классификации?", options = listOf("KNN", "SQL", "HTML"), correctAnswer = 0)
                )
            ),
            Course(
                id = 7,
                name = "JavaScript для веб-разработки",
                slug = "javascript-web-dev",
                description = "Основы JavaScript и современной веб-разработки",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
                price = 29.99,
                category = "Веб-разработка",
                videos = listOf(
                    Video(id = 13, course = 7, title = "Основы JavaScript", url = "https://coursevideo.com/js/1"),
                    Video(id = 14, course = 7, title = "DOM манипуляции", url = "https://coursevideo.com/js/2")
                ),
                tests = listOf(
                    Test(id = 7, question = "Что такое DOM?", options = listOf("Document Object Model", "Data Object Model", "Digital Object Model"), correctAnswer = 0)
                )
            ),
            Course(
                id = 8,
                name = "React для профессионалов",
                slug = "react-advanced",
                description = "Продвинутые концепции разработки на React",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/a/a7/React-icon.svg",
                price = 44.99,
                category = "Веб-разработка",
                videos = listOf(
                    Video(id = 15, course = 8, title = "Хуки в React", url = "https://coursevideo.com/react/1"),
                    Video(id = 16, course = 8, title = "Redux и управление состоянием", url = "https://coursevideo.com/react/2")
                ),
                tests = listOf(
                    Test(id = 8, question = "Для чего используется Redux?", options = listOf("Управление состоянием", "Маршрутизация", "Стилизация"), correctAnswer = 0)
                )
            ),
            Course(
                id = 9,
                name = "Docker и Kubernetes",
                slug = "docker-kubernetes",
                description = "Контейнеризация и оркестрация приложений",
                imageUrl = "https://www.docker.com/sites/default/files/d8/2019-07/vertical-logo-monochromatic.png",
                price = 54.99,
                category = "DevOps",
                videos = listOf(
                    Video(id = 17, course = 9, title = "Основы Docker", url = "https://coursevideo.com/docker/1"),
                    Video(id = 18, course = 9, title = "Kubernetes в действии", url = "https://coursevideo.com/docker/2")
                ),
                tests = listOf(
                    Test(id = 9, question = "Что такое контейнер?", options = listOf("Изолированная среда", "База данных", "Фреймворк"), correctAnswer = 0)
                )
            ),
            Course(
                id = 10,
                name = "Flutter разработка",
                slug = "flutter-dev",
                description = "Создание кроссплатформенных приложений с Flutter",
                imageUrl = "https://storage.googleapis.com/cms-storage-bucket/6a07d8a62f4308d2b854.svg",
                price = 39.99,
                category = "Мобильная разработка",
                videos = listOf(
                    Video(id = 19, course = 10, title = "Введение во Flutter", url = "https://coursevideo.com/flutter/1"),
                    Video(id = 20, course = 10, title = "Работа с виджетами", url = "https://coursevideo.com/flutter/2")
                ),
                tests = listOf(
                    Test(id = 10, question = "На каком языке пишут Flutter приложения?", options = listOf("Dart", "JavaScript", "Swift"), correctAnswer = 0)
                )
            )
        )
    }
}
