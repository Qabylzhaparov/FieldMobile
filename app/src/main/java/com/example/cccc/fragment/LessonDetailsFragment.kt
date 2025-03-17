package com.example.cccc.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cccc.databinding.FragmentLessonDetailsBinding
import com.example.cccc.entity.Video
import com.example.cccc.model.Test
import com.example.cccc.model.TestRepository
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class LessonDetailsFragment : Fragment() {

    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!
    
    private var player: ExoPlayer? = null
    private var video: Video? = null
    private var test: Test? = null
    
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
        
        // Получаем видео из аргументов
        video = arguments?.getParcelable("video")
        
        // Получаем тест для текущего урока
        video?.let { video ->
            Log.d("LessonDetailsFragment", "Video ID: ${video.id}")
            Log.d("LessonDetailsFragment", "Video Title: ${video.title}")
            test = TestRepository.getTestForLesson(video.id.toString()) // Используем ID видео напрямую
            Log.d("LessonDetailsFragment", "Test found: ${test != null}")
            setupTestSection()
        }
        
        setupVideoPlayer()
        updateUI()
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

    private fun setupVideoPlayer() {
        video?.let { video ->
            binding.lessonTitle.text = video.title
            
            player = ExoPlayer.Builder(requireContext()).build().apply {
                setMediaItem(MediaItem.fromUri(video.url))
                prepare()
            }
            
            binding.videoPlayer.player = player
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

    private fun updateUI() {
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