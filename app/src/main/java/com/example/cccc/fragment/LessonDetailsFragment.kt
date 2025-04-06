package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cccc.R
import com.example.cccc.database.AppDatabase
import com.example.cccc.databinding.FragmentLessonDetailsBinding
import com.example.cccc.model.Test
import com.example.cccc.model.TestRepository
import com.example.cccc.service.ProgressService
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class LessonDetailsFragment : Fragment() {
    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var player: ExoPlayer
    private lateinit var progressService: ProgressService
    private lateinit var appDatabase: AppDatabase
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
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
        
        appDatabase = AppDatabase.getDatabase(requireContext())
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        progressService = ProgressService(appDatabase, firestore, auth)

        val lessonId = arguments?.getString("lessonId") ?: return
        val courseId = arguments?.getString("courseId") ?: return

        setupToolbar()
        setupPlayer(lessonId)
        setupTest(lessonId)
        loadLessonProgress(lessonId, courseId)
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupPlayer(lessonId: String) {
        player = ExoPlayer.Builder(requireContext()).build()
        binding.playerView.player = player

        // Загружаем видео из Firestore
        firestore.collection("lessons")
            .document(lessonId)
            .get()
            .addOnSuccessListener { document ->
                val videoUrl = document.getString("videoUrl") ?: return@addOnSuccessListener
                val title = document.getString("title") ?: ""
                
                binding.videoTitle.text = title
                val mediaItem = MediaItem.fromUri(videoUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.playWhenReady = true

                // Отслеживаем завершение видео
                player.addListener(object : com.google.android.exoplayer2.Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == com.google.android.exoplayer2.Player.STATE_ENDED) {
                            updateVideoProgress(lessonId)
                        }
                    }
                })
            }
    }

    private fun setupTest(lessonId: String) {
        test = TestRepository.getTestForLesson(lessonId)
        if (test != null) {
            binding.testTitle.visibility = View.VISIBLE
            binding.questionCounter.visibility = View.VISIBLE
            binding.questionText.visibility = View.VISIBLE
            binding.answerOptions.visibility = View.VISIBLE
            binding.navigationButtons.visibility = View.VISIBLE
            showNextQuestion()
        } else {
            binding.testTitle.visibility = View.GONE
            binding.questionCounter.visibility = View.GONE
            binding.questionText.visibility = View.GONE
            binding.answerOptions.visibility = View.GONE
            binding.navigationButtons.visibility = View.GONE
        }
    }

    private fun showNextQuestion() {
        test?.let { test ->
            if (currentQuestionIndex < test.questions.size) {
                binding.apply {
                    questionCounter.text = "Question ${currentQuestionIndex + 1}/${test.questions.size}"
                    questionText.text = test.questions[currentQuestionIndex].text

                    // Очищаем предыдущие ответы
                    answerOptions.removeAllViews()

                    // Добавляем варианты ответов
                    test.questions[currentQuestionIndex].options.forEachIndexed { index, option ->
                        val radioButton = RadioButton(requireContext()).apply {
                            id = View.generateViewId()
                            text = option
                            setPadding(16, 16, 16, 16)
                            setBackgroundResource(R.drawable.bg_option)
                            setTextColor(resources.getColor(android.R.color.black, null))
                        }
                        answerOptions.addView(radioButton)
                    }

                    // Обновляем состояние кнопок навигации
                    previousButton.isEnabled = currentQuestionIndex > 0
                    nextButton.isEnabled = answerOptions.checkedRadioButtonId != -1
                }
            } else {
                showQuizResults()
            }
        }
    }

    private fun setupClickListeners() {
        binding.apply {
            answerOptions.setOnCheckedChangeListener { _, _ ->
                nextButton.isEnabled = true
            }

            previousButton.setOnClickListener {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--
                    showNextQuestion()
                }
            }

            nextButton.setOnClickListener {
                val selectedId = answerOptions.checkedRadioButtonId
                if (selectedId != -1) {
                    val selectedIndex = answerOptions.indexOfChild(answerOptions.findViewById(selectedId))
                    answers.add(selectedIndex)
                    currentQuestionIndex++
                    showNextQuestion()
                }
            }

            retryButton.setOnClickListener {
                resetTest()
            }

            exitButton.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun showQuizResults() {
        test?.let { test ->
            binding.apply {
                testTitle.visibility = View.GONE
                questionCounter.visibility = View.GONE
                questionText.visibility = View.GONE
                answerOptions.visibility = View.GONE
                navigationButtons.visibility = View.GONE
                resultsSection.visibility = View.VISIBLE
                retryButton.visibility = View.VISIBLE
                exitButton.visibility = View.VISIBLE

                val score = answers.count { it == test.questions[it].correctAnswerIndex }
                val totalQuestions = test.questions.size
                
                scoreText.text = "$score/$totalQuestions"
                scoreDescription.text = when {
                    score >= totalQuestions * 0.7 -> "Great job! You've passed the test!"
                    else -> "Try again to pass the test"
                }

                // Обновляем прогресс теста
                val lessonId = arguments?.getString("lessonId") ?: return
                val courseId = arguments?.getString("courseId") ?: return
                val userId = auth.currentUser?.uid ?: return

                lifecycleScope.launch {
                    progressService.updateLessonProgress(
                        userId = userId,
                        courseId = courseId,
                        lessonId = lessonId,
                        isCompleted = score >= totalQuestions * 0.7,
                        testsPassed = 1
                    )
                }
            }
        }
    }

    private fun resetTest() {
        currentQuestionIndex = 0
        answers.clear()
        binding.apply {
            resultsSection.visibility = View.GONE
            retryButton.visibility = View.GONE
            exitButton.visibility = View.GONE
            testTitle.visibility = View.VISIBLE
            questionCounter.visibility = View.VISIBLE
            questionText.visibility = View.VISIBLE
            answerOptions.visibility = View.VISIBLE
            navigationButtons.visibility = View.VISIBLE
        }
        showNextQuestion()
    }

    private fun updateVideoProgress(lessonId: String) {
        val courseId = arguments?.getString("courseId") ?: return
        val userId = auth.currentUser?.uid ?: return

        lifecycleScope.launch {
            progressService.updateLessonProgress(
                userId = userId,
                courseId = courseId,
                lessonId = lessonId,
                isCompleted = true,
                videosWatched = 1
            )
        }
    }

    private fun loadLessonProgress(lessonId: String, courseId: String) {
        val userId = auth.currentUser?.uid ?: return

        lifecycleScope.launch {
            val progress = progressService.getLessonProgress(userId, courseId, lessonId)
            progress?.let {
                if (it.isCompleted) {
                    binding.completionMessage.visibility = View.VISIBLE
                    binding.completionMessage.text = "Урок завершен"
                } else {
                    binding.completionMessage.visibility = View.VISIBLE
                    binding.completionMessage.text = "Прогресс: ${it.videosWatched} видео просмотрено, ${it.testsPassed} тестов пройдено"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player.release()
        _binding = null
    }
}
