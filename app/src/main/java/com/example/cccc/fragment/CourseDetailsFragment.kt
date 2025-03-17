package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.cccc.R
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
        
        course = arguments?.getParcelable(ARG_COURSE)
        
        setupRecyclerView()
        setupClickListeners()
        displayCourseInfo()
    }

    private fun displayCourseInfo() {
        course?.let { course ->
            with(binding) {
                Glide.with(requireContext())
                    .load(course.imageUrl)
                    .into(courseImage)

                courseTitle.text = course.name
                coursePrice.text = "$${course.price}"
                courseDuration.text = "${course.videos.size} lessons"
                aboutDescription.text = course.description

                loadLessons(course.videos)
            }
        }
    }

    private fun setupRecyclerView() {
        lessonsAdapter = LessonsAdapter { lesson ->
            if (lesson.isLocked) {
                showToast("This lesson is locked. Please purchase the course to unlock.")
            } else {
                course?.videos?.find { it.id == lesson.id }?.let { video ->
                    val bundle = Bundle().apply {
                        putParcelable("video", video)
                    }
                    findNavController().navigate(R.id.action_courseDetails_to_lessonDetails, bundle)
                }
            }
        }
        binding.lessonsRecyclerView.adapter = lessonsAdapter
    }

    private fun setupClickListeners() {
        with(binding) {
            buyButton.setOnClickListener {
                showToast("Processing purchase...")
            }

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
                duration = "10:00",
                isLocked = index > 2
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