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
        ),
        "5" to Test(
            id = "test_5",
            lessonId = "5",
            questions = listOf(
                Question(
                    "What is the main benefit of this technology?",
                    listOf("Speed", "Security", "Cost", "Popularity"),
                    0
                ),
                Question(
                    "Which language is used here?",
                    listOf("Kotlin", "Java", "Python", "C++"),
                    1
                ),
                Question(
                    "What is the recommended IDE?",
                    listOf("Android Studio", "VS Code", "IntelliJ IDEA", "Eclipse"),
                    0
                )
            )
        ),
        "6" to Test(
            id = "test_6",
            lessonId = "6",
            questions = listOf(
                Question(
                    "What is the first step in the process?",
                    listOf("Planning", "Coding", "Testing", "Deployment"),
                    0
                ),
                Question(
                    "Which tool is best for version control?",
                    listOf("Git", "SVN", "Mercurial", "Dropbox"),
                    0
                ),
                Question(
                    "What is CI/CD?",
                    listOf("Continuous Integration/Continuous Deployment", "Code Inspection/Code Debugging", "Customer Interaction/Customer Delivery", "None"),
                    0
                )
            )
        ),
        "8" to Test(
            id = "test_8",
            lessonId = "8",
            questions = listOf(
                Question(
                    "What is the output of 2+2?",
                    listOf("3", "4", "5", "22"),
                    1
                ),
                Question(
                    "Which is a valid variable name?",
                    listOf("1var", "var1", "var-1", "var.1"),
                    1
                ),
                Question(
                    "What is the extension for Kotlin files?",
                    listOf(".kt", ".java", ".py", ".cpp"),
                    0
                )
            )
        ),
        "9" to Test(
            id = "test_9",
            lessonId = "9",
            questions = listOf(
                Question(
                    "What is Android's main UI component?",
                    listOf("Activity", "Service", "BroadcastReceiver", "ContentProvider"),
                    0
                ),
                Question(
                    "Which layout is most flexible?",
                    listOf("ConstraintLayout", "LinearLayout", "FrameLayout", "RelativeLayout"),
                    0
                ),
                Question(
                    "What is used for background tasks?",
                    listOf("Service", "Activity", "Fragment", "Intent"),
                    0
                )
            )
        ),
        "10" to Test(
            id = "test_10",
            lessonId = "10",
            questions = listOf(
                Question(
                    "What is the best practice for user authentication?",
                    listOf("OAuth", "Plain text", "No authentication", "Basic Auth"),
                    0
                ),
                Question(
                    "Which database is used in Android?",
                    listOf("Room", "MySQL", "MongoDB", "PostgreSQL"),
                    0
                ),
                Question(
                    "What is the recommended way to store images?",
                    listOf("Firebase Storage", "SQLite", "SharedPreferences", "Assets"),
                    0
                )
            )
        ),
        "11" to Test(
            id = "test_11",
            lessonId = "11",
            questions = listOf(
                Question(
                    "What is the main function of a ViewModel?",
                    listOf("UI logic", "Data storage", "Networking", "Database access"),
                    0
                ),
                Question(
                    "Which lifecycle method is called first?",
                    listOf("onCreate", "onStart", "onResume", "onPause"),
                    0
                ),
                Question(
                    "What is LiveData used for?",
                    listOf("Observing data changes", "Saving files", "Drawing UI", "Networking"),
                    0
                )
            )
        ),
        "12" to Test(
            id = "test_12",
            lessonId = "12",
            questions = listOf(
                Question(
                    "What is the main advantage of coroutines?",
                    listOf("Asynchronous code", "Faster UI", "Better graphics", "More memory"),
                    0
                ),
                Question(
                    "Which scope is used in ViewModel?",
                    listOf("viewModelScope", "lifecycleScope", "globalScope", "mainScope"),
                    0
                ),
                Question(
                    "What is suspend function?",
                    listOf("A function that can be paused and resumed", "A function that is deprecated", "A function that is static", "A function that is private"),
                    0
                )
            )
        ),
        "13" to Test(
            id = "test_13",
            lessonId = "13",
            questions = listOf(
                Question(
                    "What is the main use of RecyclerView?",
                    listOf("Display lists", "Store data", "Send network requests", "Draw shapes"),
                    0
                ),
                Question(
                    "Which adapter is used with RecyclerView?",
                    listOf("RecyclerView.Adapter", "ArrayAdapter", "BaseAdapter", "SimpleAdapter"),
                    0
                ),
                Question(
                    "What is ViewHolder pattern?",
                    listOf("Optimizing view reuse", "Drawing UI", "Saving data", "Handling clicks"),
                    0
                )
            )
        ),
        "14" to Test(
            id = "test_14",
            lessonId = "14",
            questions = listOf(
                Question(
                    "What is the main purpose of intents?",
                    listOf("Communication between components", "Drawing UI", "Saving data", "Networking"),
                    0
                ),
                Question(
                    "Which intent is used to start an activity?",
                    listOf("Explicit intent", "Implicit intent", "Broadcast intent", "Service intent"),
                    0
                ),
                Question(
                    "What is putExtra used for?",
                    listOf("Passing data", "Drawing UI", "Saving files", "Networking"),
                    0
                )
            )
        ),
        "15" to Test(
            id = "test_15",
            lessonId = "15",
            questions = listOf(
                Question(
                    "What is the main use of SharedPreferences?",
                    listOf("Store key-value pairs", "Draw UI", "Send emails", "Networking"),
                    0
                ),
                Question(
                    "Which method saves data in SharedPreferences?",
                    listOf("apply()", "commit()", "save()", "put()"),
                    0
                ),
                Question(
                    "What is the file format of SharedPreferences?",
                    listOf("XML", "JSON", "TXT", "CSV"),
                    0
                )
            )
        )
    )

    fun getTestForLesson(lessonId: String): Test? {
        return tests[lessonId]
    }
} 