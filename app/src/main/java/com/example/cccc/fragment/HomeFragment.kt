package com.example.cccc.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.R
import com.example.cccc.adapter.CourseAdapter
import com.example.cccc.databinding.FragmentHomeBinding
import com.example.cccc.databinding.FragmentLessonDetailsBinding
import com.example.cccc.db.CourseRepositoryLocal
import com.example.cccc.entity.Course
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var courseAdapter: CourseAdapter
    
    private var appStartTime: Long = 0
    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            updateLearnedTime()
            handler.postDelayed(this, 60000) // Обновляем каждую минуту
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        
        setupUserInfo()
        setupClickListeners()
        setupLearningPlan()
        startTimeTracking()
    }

    private fun setupUserInfo() {
        auth.currentUser?.let { user ->
            binding.tvUserName.text = "Hi, ${user.displayName ?: "Student"}"
        }
    }

    private fun setupClickListeners() {
        // Переход в профиль
        binding.ivProfile.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_profile)
        }

        // Переход к курсам
        binding.btnGetStarted.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_courses)
        }

        // Переход к AI ассистенту
        binding.aiAssistantCard.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_chat)
        }
    }

    private fun setupLearningPlan() {
        courseAdapter = CourseAdapter { course ->
            val bundle = Bundle().apply {
                putParcelable("course", course)
            }
            findNavController().navigate(R.id.action_home_to_courseDetails, bundle)
        }

        binding.rvLearningPlan.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = courseAdapter
        }

        val allCourses = CourseRepositoryLocal.getCourses()
        val specificCourses = listOf(allCourses[7], allCourses[3])
        courseAdapter.submitList(specificCourses)
    }

    private fun startTimeTracking() {
        // Получаем сохраненное время начала из SharedPreferences
        val prefs = requireActivity().getSharedPreferences("app_prefs", 0)
        appStartTime = prefs.getLong("start_time", 0)
        
        // Если это первый запуск сегодня
        if (!isSameDay(Date(appStartTime), Date())) {
            appStartTime = System.currentTimeMillis()
            prefs.edit().putLong("start_time", appStartTime).apply()
        }
        
        // Запускаем обновление времени
        updateLearnedTime()
        handler.postDelayed(updateTimeRunnable, 60000)
    }

    private fun updateLearnedTime() {
        val currentTime = System.currentTimeMillis()
        val learnedMinutes = TimeUnit.MILLISECONDS.toMinutes(currentTime - appStartTime)
        
        binding.tvProgress.text = "${learnedMinutes}min"
        binding.progressBar.progress = (learnedMinutes % 60).toInt()
    }

    private fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance().apply { time = date1 }
        val cal2 = Calendar.getInstance().apply { time = date2 }
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTimeRunnable)
        _binding = null
    }
}