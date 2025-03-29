package com.example.cccc.model

data class Test(
    val id: String,
    val lessonId: String,
    val questions: List<Question>
)

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int
)

object TestRepository {
    private val tests = mapOf(
        "1" to Test(
            id = "test_1",
            lessonId = "1",
            questions = listOf(
                Question(
                    "What is the main purpose of this lesson?",
                    listOf("Understanding basics", "Advanced concepts", "Practical examples", "Theory only"),
                    0
                ),
                Question(
                    "Which concept is most important in this topic?",
                    listOf("Performance", "Simplicity", "Complexity", "Documentation"),
                    0
                ),
                Question(
                    "What is the best practice for implementing this feature?",
                    listOf("Direct approach", "Best practices", "Quick solution", "Trial and error"),
                    0
                ),
                Question(
                    "Which approach would you recommend for this scenario?",
                    listOf("Traditional", "Modern", "Hybrid", "Experimental"),
                    0
                ),
                Question(
                    "What are the key takeaways from this lesson?",
                    listOf("Speed", "Quality", "Efficiency", "Innovation"),
                    0
                )
            )
        ),
        "2" to Test(
            id = "test_2",
            lessonId = "2",
            questions = listOf(
                Question(
                    "What is the primary focus of this lesson?",
                    listOf("Theory", "Practice", "Both", "Neither"),
                    2
                ),
                Question(
                    "Which tool is essential for this topic?",
                    listOf("Basic tools", "Advanced tools", "No tools", "All tools"),
                    0
                ),
                Question(
                    "What is the recommended approach?",
                    listOf("Fast", "Thorough", "Random", "None"),
                    1
                ),
                Question(
                    "Which method is most effective?",
                    listOf("Method A", "Method B", "Method C", "Method D"),
                    1
                ),
                Question(
                    "What should you focus on first?",
                    listOf("Basics", "Advanced", "Everything", "Nothing"),
                    0
                )
            )
        ),

        "4" to Test(
            id = "test_4",
            lessonId = "4",
            questions = listOf(
                Question(
                    "OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO",
                    listOf("Theory", "Practice", "Both", "Neither"),
                    0
                )
            )
        ),
        "7" to Test(
            id = "test_4",
            lessonId = "4",
            questions = listOf(
                Question(
                    "OOOOOOOOOOIIIIIIIIIIIIOO",
                    listOf("Theory", "Practice", "Both", "Neither"),
                    0
                )
            )
        ),
        "3" to Test(
            id = "test_3",
            lessonId = "3",
            questions = listOf(
                Question(
                    "What is the key concept here?",
                    listOf("Concept A", "Concept B", "Concept C", "Concept D"),
                    1
                ),
                Question(
                    "Which principle is most important?",
                    listOf("Principle 1", "Principle 2", "Principle 3", "Principle 4"),
                    2
                ),
                Question(
                    "What should you avoid?",
                    listOf("Mistake A", "Mistake B", "Mistake C", "Mistake D"),
                    0
                ),
                Question(
                    "Which technique is recommended?",
                    listOf("Technique 1", "Technique 2", "Technique 3", "Technique 4"),
                    3
                ),
                Question(
                    "What is the best practice?",
                    listOf("Practice A", "Practice B", "Practice C", "Practice D"),
                    1
                )
            )
        )
    )

    fun getTestForLesson(lessonId: String): Test? {
        return tests[lessonId]
    }
} 