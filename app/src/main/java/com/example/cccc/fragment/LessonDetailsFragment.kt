package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cccc.databinding.FragmentLessonDetailsBinding
import com.example.cccc.entity.Video
import com.example.cccc.model.Test
import com.example.cccc.model.TestRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class LessonDetailsFragment : Fragment() {

    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!
    
    private var player: ExoPlayer? = null
    private lateinit var video: Video
    private var test: Test? = null
    private var lessonId: String? = null
    private var isLessonCompleted = false
    
    private var currentQuestionIndex = 0
    private val answers = mutableListOf<Int>()

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Инициализируем Firebase
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        
        video = arguments?.getParcelable("video") ?: throw IllegalStateException("Video is required")
        lessonId = arguments?.getString("lessonId")
        
        // Получаем тест для текущего урока
        test = TestRepository.getTestForLesson(video.id.toString())
        
        setupToolbar()
        setupPlayer()
        setupTestSection()
        updateUI()
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            if (!isLessonCompleted) {
                // Если урок не завершен, показываем диалог подтверждения
                showExitConfirmationDialog()
            } else {
                // Если урок завершен, просто возвращаемся назад
                findNavController().navigateUp()
            }
        }
    }

    private fun setupPlayer() {
        player = ExoPlayer.Builder(requireContext()).build().apply {
            setMediaItem(MediaItem.fromUri(video.url))
            prepare()
            
            // Добавляем слушатель для отслеживания завершения видео
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(state: Int) {
                    if (state == Player.STATE_ENDED) {
                        // Видео завершено
                        onVideoCompleted()
                    }
                }
            })
        }
        
        binding.playerView.player = player
    }

    private fun setupTestSection() {
        with(binding) {
            if (test != null) {
                // Показываем секцию с тестом
                testTitle.visibility = View.VISIBLE
                questionCounter.visibility = View.VISIBLE
                questionText.visibility = View.VISIBLE
                answerOptions.visibility = View.VISIBLE
                navigationButtons.visibility = View.VISIBLE
                resultsSection.visibility = View.GONE
                
                setupTest()
            } else {
                // Показываем сообщение о недоступности теста
                testTitle.visibility = View.VISIBLE
                testTitle.text = "Test Not Available"
                questionCounter.visibility = View.GONE
                questionText.visibility = View.GONE
                answerOptions.visibility = View.GONE
                navigationButtons.visibility = View.GONE
                resultsSection.visibility = View.GONE
                
                Toast.makeText(requireContext(), "Test is not available for this lesson", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupTest() {
        test?.let { test ->
            with(binding) {
                // Настройка кнопок навигации
                previousButton.setOnClickListener {
                    if (currentQuestionIndex > 0) {
                        currentQuestionIndex--
                        updateUI()
                    }
                }

                nextButton.setOnClickListener {
                    val selectedOption = when (answerOptions.checkedRadioButtonId) {
                        option1.id -> 0
                        option2.id -> 1
                        option3.id -> 2
                        option4.id -> 3
                        else -> -1
                    }

                    if (selectedOption == -1) {
                        Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }

                    // Сохраняем ответ
                    if (currentQuestionIndex < answers.size) {
                        answers[currentQuestionIndex] = selectedOption
                    } else {
                        answers.add(selectedOption)
                    }

                    if (currentQuestionIndex < test.questions.size - 1) {
                        currentQuestionIndex++
                        updateUI()
                    } else {
                        showResults()
                    }
                }
            }
        }
    }

    private fun onVideoCompleted() {
        isLessonCompleted = true
        // Уведомляем родительский фрагмент о завершении урока
        lessonId?.let { (parentFragment as? CourseDetailsFragment)?.onLessonCompleted(it) }
        
        // Показываем сообщение о завершении
        binding.completionMessage.visibility = View.VISIBLE
        binding.completionMessage.text = "Lesson completed! You can now exit."
    }

    private fun updateUI() {
        binding.videoTitle.text = video.title
        
        test?.let { test ->
            with(binding) {
                // Обновляем прогресс
                progressBar.progress = ((currentQuestionIndex + 1) * 100) / test.questions.size
                
                // Обновляем счетчик вопросов
                questionCounter.text = "Question ${currentQuestionIndex + 1} of ${test.questions.size}"
                
                // Обновляем вопрос
                questionText.text = test.questions[currentQuestionIndex].text
                
                // Обновляем варианты ответов
                option1.text = test.questions[currentQuestionIndex].options[0]
                option2.text = test.questions[currentQuestionIndex].options[1]
                option3.text = test.questions[currentQuestionIndex].options[2]
                option4.text = test.questions[currentQuestionIndex].options[3]
                
                // Восстанавливаем предыдущий ответ
                if (currentQuestionIndex < answers.size) {
                    when (answers[currentQuestionIndex]) {
                        0 -> option1.isChecked = true
                        1 -> option2.isChecked = true
                        2 -> option3.isChecked = true
                        3 -> option4.isChecked = true
                    }
                } else {
                    answerOptions.clearCheck()
                }
                
                // Обновляем видимость кнопок
                previousButton.visibility = if (currentQuestionIndex > 0) View.VISIBLE else View.INVISIBLE
                nextButton.text = if (currentQuestionIndex == test.questions.size - 1) "Finish" else "Next"
            }
        }
    }

    private fun showResults() {
        test?.let { test ->
            val correctCount = answers.zip(test.questions.map { it.correctAnswerIndex })
                .count { (answer, correct) -> answer == correct }
            
            with(binding) {
                // Скрываем тест
                testTitle.visibility = View.VISIBLE
                questionCounter.visibility = View.GONE
                questionText.visibility = View.GONE
                answerOptions.visibility = View.GONE
                navigationButtons.visibility = View.GONE
                
                // Показываем результаты
                resultsSection.visibility = View.VISIBLE
                scoreText.text = "$correctCount/${test.questions.size}"
                scoreDescription.text = when {
                    correctCount == test.questions.size -> "Perfect! You've mastered this lesson!"
                    correctCount >= test.questions.size * 0.8 -> "Great job! You've passed the test!"
                    correctCount >= test.questions.size * 0.6 -> "Good effort! Keep practicing!"
                    else -> "You might want to review this lesson again."
                }

                // Показываем кнопки только если тест пройден успешно (70% или больше)
                if (correctCount >= test.questions.size * 0.7) {
                    exitButton.visibility = View.VISIBLE
                    retryButton.visibility = View.GONE
                    // Автоматически завершаем урок при успешном прохождении
                    completeLesson()
                } else {
                    // Если тест не пройден, показываем только кнопку для повторной попытки
                    exitButton.visibility = View.GONE
                    retryButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showExitConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Exit Lesson")
            .setMessage("Are you sure you want to exit? Your progress will not be saved.")
            .setPositiveButton("Exit") { _, _ ->
                findNavController().navigateUp()
            }
            .setNegativeButton("Continue", null)
            .show()
    }

    private fun setupClickListeners() {
        binding.apply {
            backButton.setOnClickListener {
                if (!isLessonCompleted) {
                    showExitConfirmationDialog()
                } else {
                    findNavController().navigateUp()
                }
            }

            exitButton.setOnClickListener {
                findNavController().navigateUp()
            }

            retryButton.setOnClickListener {
                // Сбрасываем тест для повторной попытки
                currentQuestionIndex = 0
                answers.clear()
                
                // Возвращаем видимость элементов теста
                testTitle.visibility = View.VISIBLE
                questionCounter.visibility = View.VISIBLE
                questionText.visibility = View.VISIBLE
                answerOptions.visibility = View.VISIBLE
                navigationButtons.visibility = View.VISIBLE
                
                // Скрываем результаты
                resultsSection.visibility = View.GONE
                retryButton.visibility = View.GONE
                
                // Обновляем UI теста
                updateUI()
            }
        }
    }

    private fun completeLesson() {
        val userId = auth.currentUser?.uid ?: return
        val courseId = arguments?.getString("courseId")
        val lessonId = arguments?.getString("lessonId")

        if (courseId.isNullOrEmpty() || lessonId.isNullOrEmpty()) {
            showToast("Ошибка: отсутствует courseId или lessonId")
            return
        }

        val lessonRef = db.collection("users")
            .document(userId)
            .collection("courses")
            .document(courseId)
            .collection("lessons")
            .document(lessonId)

        lessonRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                lessonRef.update("completed", true)
                    .addOnSuccessListener {
                        showToast("Lesson completed successfully!")
                        isLessonCompleted = true

                        // Обновляем прогресс курса
                        updateCourseProgress(userId, courseId)

                        // Уведомляем родительский фрагмент
                        findNavController().previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("lessonCompleted", lessonId)

                        findNavController().navigateUp()
                    }
                    .addOnFailureListener { e ->
                        showToast("Ошибка при завершении урока: ${e.message}")
                    }
            } else {
                showToast("Ошибка: урок не найден в Firestore")
            }
        }
    }


    private fun updateCourseProgress(userId: String, courseId: String) {
        val courseRef = db.collection("users")
            .document(userId)
            .collection("courses")
            .document(courseId)

        courseRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val totalLessons = document.getLong("totalLessons") ?: 0
                val completedLessons = document.getLong("completedLessons") ?: 0
                val newCompletedLessons = completedLessons + 1
                val progress = (newCompletedLessons.toDouble() / totalLessons.toDouble() * 100).toInt()

                courseRef.update(
                    "completedLessons", newCompletedLessons,
                    "progress", progress
                ).addOnSuccessListener {
                    findNavController().previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("courseProgressUpdated", progress)
                }
            }
        }
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showQuizResults(score: Int, totalQuestions: Int) {
        binding.apply {
            // Скрываем тест
            testTitle.visibility = View.VISIBLE
            questionCounter.visibility = View.GONE
            questionText.visibility = View.GONE
            answerOptions.visibility = View.GONE
            navigationButtons.visibility = View.GONE
            
            // Показываем результаты
            resultsSection.visibility = View.VISIBLE
            exitButton.visibility = View.VISIBLE
            
            scoreText.text = "$score/$totalQuestions"
            scoreDescription.text = when {
                score >= totalQuestions * 0.7 -> "Congratulations! You passed the quiz!"
                else -> "Try again to pass the quiz"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        _binding = null
    }

    companion object {
        private const val ARG_VIDEO = "video"
        
        fun newInstance(video: Video) = LessonDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_VIDEO, video)
            }
        }
    }
} 