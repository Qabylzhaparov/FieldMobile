package com.example.cccc.database

import com.example.cccc.entity.Course
import com.example.cccc.entity.Test
import com.example.cccc.entity.Video
import com.example.cccc.model.CourseCategory

object CourseRepositoryLocal {
    fun getCourses(): List<Course> {
        return listOf(
            Course(
                id = 1,
                name = "Kotlin для начинающих",
                slug = "kotlin-beginners", 
                description = "Изучение основ Kotlin с нуля",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/7/74/Kotlin_Icon.png",
                price = 19.99,
                category = CourseCategory.PROGRAMMING,
                videos = listOf(
                    Video(id = 1, course = 1, title = "Введение в Kotlin", url = "https://www.youtube.com/watch?v=H_oGi8uuDpA"),
                    Video(id = 2, course = 1, title = "Переменные и типы данных", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 3, course = 1, title = "Функции в Kotlin", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 4, course = 1, title = "Классы и объекты", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 5, course = 1, title = "Наследование и интерфейсы", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 6, course = 1, title = "Коллекции в Kotlin", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 1, question = "Что такое Kotlin?", options = listOf("Язык", "Среда", "Фреймворк"), correctAnswer = 0)
                ),
                isNew = false,
                rating = 4.5
            ),
            Course(
                id = 2,
                name = "Основы Android",
                slug = "android-basics",
                description = "Как создавать мобильные приложения на Android",
                imageUrl = "https://logowik.com/content/uploads/images/t_android-studio5850.logowik.com.webp",
                price = 29.99,
                category = CourseCategory.MOBILE,
                videos = listOf(
                    Video(id = 7, course = 2, title = "Activity и фрагменты", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 2, question = "Что такое Activity?", options = listOf("Класс", "Файл", "Метод"), correctAnswer = 1)
                ),
                isNew = false,
                rating = 4.3
            ),
            Course(
                id = 3,
                name = "Jetpack Compose с нуля",
                slug = "jetpack-compose",
                description = "Современная разработка UI в Android с помощью Jetpack Compose",
                imageUrl = " ",
                price = 34.99,
                category = CourseCategory.MOBILE,
                videos = listOf(
                    Video(id = 13, course = 3, title = "Введение в Jetpack Compose", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 14, course = 3, title = "Работа с состоянием в Compose", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 15, course = 3, title = "Composable функции", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 16, course = 3, title = "Модификаторы в Compose", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 17, course = 3, title = "Анимации в Compose", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 18, course = 3, title = "Тема и стили", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 3, question = "Какой язык используется в Jetpack Compose?", options = listOf("Java", "Kotlin", "Dart"), correctAnswer = 1)
                ),
                isNew = true,
                rating = 4.2
            ),
            Course(
                id = 4,
                name = "Spring Boot для начинающих",
                slug = "spring-boot",
                description = "Разработка серверных приложений с использованием Spring Boot",
                imageUrl = "https://logowik.com/content/uploads/images/spring-java7947.logowik.com.webp",
                price = 39.99,
                category = CourseCategory.BACKEND,
                videos = listOf(
                    Video(id = 19, course = 4, title = "Основы Spring Boot", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 20, course = 4, title = "Работа с базой данных", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 21, course = 4, title = "REST API в Spring Boot", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 22, course = 4, title = "Spring Security", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 23, course = 4, title = "Микросервисы", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 24, course = 4, title = "Тестирование в Spring Boot", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 4, question = "Какой язык используется в Spring Boot?", options = listOf("C++", "Python", "Java"), correctAnswer = 2)
                ),
                isNew = true,
                rating = 3.8
            ),
            Course(
                id = 5,
                name = "Python для анализа данных",
                slug = "python-data-analysis",
                description = "Как анализировать данные с использованием Pandas и NumPy",
                imageUrl = "https://logowik.com/content/uploads/images/python4089.logowik.com.webp",
                price = 24.99,
                category = CourseCategory.DATA_ANALYSIS,
                videos = listOf(
                    Video(id = 25, course = 5, title = "Основы Pandas", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 26, course = 5, title = "Работа с массивами в NumPy", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 27, course = 5, title = "Визуализация данных", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 28, course = 5, title = "Статистический анализ", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 29, course = 5, title = "Машинное обучение с Python", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 30, course = 5, title = "Работа с временными рядами", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 5, question = "Какой модуль используется для работы с таблицами?", options = listOf("Pandas", "NumPy", "Matplotlib"), correctAnswer = 0)
                ),
                isNew = false,
                rating = 4.9
                ),
            Course(
                id = 6,
                name = "Основы машинного обучения",
                slug = "machine-learning",
                description = "Как строить модели машинного обучения с Scikit-learn",
                imageUrl = "https://logowik.com/content/uploads/images/azure-machine-learning-service1395.jpg",
                price = 49.99,
                category = CourseCategory.MACHINE_LEARNING,
                videos = listOf(
                    Video(id = 31, course = 6, title = "Введение в машинное обучение", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 32, course = 6, title = "Обучение моделей", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 33, course = 6, title = "Нейронные сети", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 34, course = 6, title = "Обработка изображений", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 35, course = 6, title = "Обработка текста", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 36, course = 6, title = "Оценка моделей", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 6, question = "Какой метод используется для классификации?", options = listOf("KNN", "SQL", "HTML"), correctAnswer = 0)
                ),
                isNew = false,
                rating = 3.5
            ),
            Course(
                id = 7,
                name = "JavaScript для веб-разработки",
                slug = "javascript-web-dev",
                description = "Основы JavaScript и современной веб-разработки",
                imageUrl = "https://upload.wikimedia.org/wikipedia/commons/6/6a/JavaScript-logo.png",
                price = 29.99,
                category = CourseCategory.WEB_DEVELOPMENT,
                videos = listOf(
                    Video(id = 37, course = 7, title = "Основы JavaScript", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 38, course = 7, title = "DOM манипуляции", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 39, course = 7, title = "Асинхронное программирование", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 40, course = 7, title = "ES6+ возможности", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 41, course = 7, title = "Работа с API", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 42, course = 7, title = "Тестирование в JavaScript", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 7, question = "Что такое DOM?", options = listOf("Document Object Model", "Data Object Model", "Digital Object Model"), correctAnswer = 0)
                ),
                isNew = false,
                rating = 4.7
            ),
            Course(
                id = 8,
                name = "React для профессионалов",
                slug = "react-advanced",
                description = "Продвинутые концепции разработки на React",
                imageUrl = "https://logowik.com/content/uploads/images/react7473.logowik.com.webp",
                price = 44.99,
                category = CourseCategory.WEB_DEVELOPMENT,
                videos = listOf(
                    Video(id = 43, course = 8, title = "Хуки в React", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 44, course = 8, title = "Redux и управление состоянием", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 45, course = 8, title = "React Router", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 46, course = 8, title = "Оптимизация производительности", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 47, course = 8, title = "Тестирование React приложений", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 48, course = 8, title = "TypeScript в React", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 8, question = "Для чего используется Redux?", options = listOf("Управление состоянием", "Маршрутизация", "Стилизация"), correctAnswer = 0)
                ),
                isNew = true,
                rating = 4.5
            ),
            Course(
                id = 9,
                name = "Docker и Kubernetes",
                slug = "docker-kubernetes",
                description = "Контейнеризация и оркестрация приложений",
                imageUrl = "https://logowik.com/content/uploads/images/t_301_docker.jpg",
                price = 54.99,
                category = CourseCategory.DEVOPS,
                videos = listOf(
                    Video(id = 49, course = 9, title = "Основы Docker", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 50, course = 9, title = "Kubernetes в действии", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 51, course = 9, title = "Docker Compose", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 52, course = 9, title = "Сетевая модель Docker", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 53, course = 9, title = "Мониторинг контейнеров", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 54, course = 9, title = "CI/CD с Docker", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 9, question = "Что такое контейнер?", options = listOf("Изолированная среда", "База данных", "Фреймворк"), correctAnswer = 0)
                ),
                isNew = false,
                rating =  4.5
            ),
            Course(
                id = 10,
                name = "Flutter разработка",
                slug = "flutter-dev",
                description = "Создание кроссплатформенных приложений с Flutter",
                imageUrl = "https://logowik.com/content/uploads/images/t_flutter5786.jpg",
                price = 39.99,
                category = CourseCategory.MOBILE,
                videos = listOf(
                    Video(id = 55, course = 10, title = "Введение во Flutter", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 56, course = 10, title = "Работа с виджетами", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 57, course = 10, title = "Навигация в Flutter", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 58, course = 10, title = "Управление состоянием", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 59, course = 10, title = "Работа с API", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U"),
                    Video(id = 60, course = 10, title = "Публикация приложения", url = "https://www.youtube.com/watch?v=8Xg7E9shq0U")
                ),
                tests = listOf(
                    Test(id = 10, question = "На каком языке пишут Flutter приложения?", options = listOf("Dart", "JavaScript", "Swift"), correctAnswer = 0)
                ),
                isNew = true,
                rating = 4.6
            )
        )
    }
}
