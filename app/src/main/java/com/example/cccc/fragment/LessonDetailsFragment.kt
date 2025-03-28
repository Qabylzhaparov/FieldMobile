package com.example.cccc.fragment

import android.os.Bundle
import android.util.Log
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
import com.google.android.exoplayer2.ui.PlayerView

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
        
        video = arguments?.getParcelable("video") ?: throw IllegalStateException("Video is required")
        lessonId = arguments?.getString("lessonId")
        
        // Получаем тест для текущего урока
        test = TestRepository.getTestForLesson(video.id.toString())
        
        setupToolbar()
        setupPlayer()
        setupTestSection()
        updateUI()

        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

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
        (parentFragment as? CourseDetailsFragment)?.onLessonCompleted(lessonId)
        
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