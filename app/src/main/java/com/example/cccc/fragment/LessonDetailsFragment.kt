package com.example.cccc.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cccc.R
import com.example.cccc.database.AppDatabase
import com.example.cccc.database.CourseRepositoryLocal
import com.example.cccc.databinding.FragmentLessonDetailsBinding
import com.example.cccc.entity.Video
import com.example.cccc.model.Test
import com.example.cccc.model.TestRepository
import com.example.cccc.service.ProgressService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.coroutines.launch

class LessonDetailsFragment : Fragment() {
    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var youTubePlayer: YouTubePlayerView
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
        setupYouTubePlayer(lessonId)
        setupTest(lessonId)
        loadLessonProgress(lessonId, courseId)
        setupClickListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupYouTubePlayer(lessonId: String) {

        val video = arguments?.getParcelable<Video>("video") ?: return

        binding.videoTitle.text = video.title

        val videoId = extractVideoId(video.url)
        if (videoId == null) {
            Toast.makeText(requireContext(), "Неверный формат URL видео", Toast.LENGTH_LONG).show()
            return
        }

        youTubePlayer = binding.youtubePlayer // инициализация
        lifecycle.addObserver(youTubePlayer)
        youTubePlayer.enableAutomaticInitialization = false

        youTubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoId, 0f)

                youTubePlayer.addListener(object : AbstractYouTubePlayerListener() {
                    override fun onStateChange(youTubePlayer: YouTubePlayer, state: PlayerConstants.PlayerState) {
                        if (state == PlayerConstants.PlayerState.ENDED) {
                            updateVideoProgress(lessonId)
                        }
                    }
                })
            }
        })
    }

    private fun extractVideoId(url: String): String? {
        val patterns = listOf(
            "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*",
            "(?<=v=)[^#\\&\\?\\n]*",
            "(?<=youtu.be/)[^#\\&\\?\\n]*"
        )

        for (pattern in patterns) {
            val matcher = java.util.regex.Pattern.compile(pattern).matcher(url)
            if (matcher.find()) {
                return matcher.group()
            }
        }
        return null
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

    val Int.dp: Int get() = (this * resources.displayMetrics.density).toInt()

    private fun showNextQuestion() {
        test?.let { test ->
            if (currentQuestionIndex < test.questions.size) {
                binding.apply {
                    questionCounter.text = "Question ${currentQuestionIndex + 1}/${test.questions.size}"
                    questionText.text = test.questions[currentQuestionIndex].text

                    answerOptions.removeAllViews()

                    test.questions[currentQuestionIndex].options.forEachIndexed { index, option ->
                        val radioButton = RadioButton(requireContext()).apply {
                            id = View.generateViewId()
                            text = option
                            setPadding(16, 32, 16, 32)
                            setBackgroundResource(R.drawable.bg_option)
                            setTextColor(resources.getColor(android.R.color.black, null))
                            layoutParams = RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT
                            ).apply {
                                bottomMargin = 8.dp // если нужен отступ, используйте extension для dp
                            }
                        }
                        answerOptions.addView(radioButton)
                    }

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
        youTubePlayer.release()
        _binding = null
    }
}
