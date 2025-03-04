package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cccc.adapter.LessonsAdapter
import com.example.cccc.databinding.FragmentCourseDetailsBinding
import com.example.cccc.entity.Course
import com.example.cccc.entity.Video
import com.example.cccc.model.Lesson

class CourseDetailsFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!
    
    private var isFavorite = false
    private lateinit var lessonsAdapter: LessonsAdapter
    private var course: Course? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Получаем курс из аргументов
        course = arguments?.getParcelable(ARG_COURSE)
        
        setupRecyclerView()
        setupClickListeners()
        displayCourseInfo()
    }

    private fun displayCourseInfo() {
        course?.let { course ->
            with(binding) {
                // Загружаем изображение курса
                Glide.with(requireContext())
                    .load(course.imageUrl)
                    .into(courseImage)

                // Устанавливаем информацию о курсе
                courseTitle.text = course.name
                coursePrice.text = "$${course.price}"
                courseDuration.text = "${course.videos.size} lessons"
                aboutDescription.text = course.description

                // Загружаем видео курса
                loadLessons(course.videos)
            }
        }
    }

    private fun setupRecyclerView() {
        lessonsAdapter = LessonsAdapter { lesson ->
            if (lesson.isLocked) {
                showToast("This lesson is locked. Please purchase the course to unlock.")
            } else {
                showToast("Playing: ${lesson.title}")
            }
        }
        binding.lessonsRecyclerView.adapter = lessonsAdapter
    }

    private fun setupClickListeners() {
        with(binding) {
            // Buy button click listener
            buyButton.setOnClickListener {
                showToast("Processing purchase...")
            }

            // Favorite button click listener
            favoriteButton.setOnClickListener {
                isFavorite = !isFavorite
                favoriteButton.setColorFilter(
                    if (isFavorite) 0xFFFFE24D.toInt() else 0xFF757575.toInt()
                )
                showToast(if (isFavorite) "Added to favorites" else "Removed from favorites")
            }
        }
    }

    private fun loadLessons(videos: List<Video>) {
        val lessons = videos.mapIndexed { index, video ->
            Lesson(
                id = video.id,
                number = String.format("%02d", index + 1),
                title = video.title,
                duration = "10:00", // Здесь можно добавить реальную длительность видео
                isLocked = index > 2 // Первые 3 видео доступны бесплатно
            )
        }
        lessonsAdapter.setLessons(lessons)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_COURSE = "course"
        
        fun newInstance(course: Course) = CourseDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_COURSE, course)
            }
        }
    }
} 