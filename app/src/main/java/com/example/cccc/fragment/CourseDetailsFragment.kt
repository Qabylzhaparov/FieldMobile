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
import com.example.cccc.adapter.LessonAdapter
import com.example.cccc.databinding.FragmentCourseDetailsBinding
import com.example.cccc.entity.Course
import com.example.cccc.entity.Video
import com.example.cccc.model.Lesson
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class CourseDetailsFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!
    
    private var isFavorite = false
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var course: Course
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    
    private var completedLessons = mutableListOf<String>()
    private var totalLessons = 0

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
        
        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        
        course = arguments?.getParcelable("course") ?: throw IllegalStateException("Course is required")
        
        setupToolbar()
        setupRecyclerView()
        loadCourseProgress()
        updateUI()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        lessonAdapter = LessonAdapter { lesson ->
            if (lesson.isLocked) {
                showToast("This lesson is locked. Please purchase the course to unlock.")
            } else {
                course.videos.find { it.id.toString() == lesson.id }?.let { video ->
                    val bundle = Bundle().apply {
                        putParcelable("video", video)
                        putString("lessonId", lesson.id)
                    }
                    findNavController().navigate(R.id.action_courseDetails_to_lessonDetails, bundle)
                }
            }
        }
        binding.lessonsRecyclerView.adapter = lessonAdapter

        // Конвертируем List<Video> в List<Lesson>
        val lessonList = course.videos.mapIndexed { index, video ->
            Lesson(
                id = video.id.toString(),
                number = index + 1,
                title = video.title,
                duration = "Unknown",
                isLocked = false,
                videoUrl = video.url
            )
        }

        lessonAdapter.submitList(lessonList)
        totalLessons = lessonList.size
    }

    private fun loadCourseProgress() {
        val userId = auth.currentUser?.uid ?: return
        val courseId = course.id
        
        db.collection("users")
            .document(userId)
            .collection("course_progress")
            .document(courseId.toString())
            .get()
            .addOnSuccessListener { document ->
                val savedCompletedLessons = document.get("completed_lessons") as? List<String> ?: emptyList()
                completedLessons.clear()
                completedLessons.addAll(savedCompletedLessons)
                updateProgressUI()
            }
    }

    private fun updateProgressUI() {
        val progress = if (totalLessons > 0) {
            (completedLessons.size.toFloat() / totalLessons.toFloat()) * 100
        } else {
            0f
        }
        
        binding.progressIndicator.progress = progress.toInt()
        binding.progressText.text = "${progress.toInt()}%"
        binding.progressDescription.text = "$completedLessons of $totalLessons lessons completed"
        
        binding.getCertificateButton.visibility = if (progress >= 100) View.VISIBLE else View.GONE
    }

    private fun updateUI() {
        with(binding) {
            Glide.with(requireContext())
                .load(course.imageUrl)
                .into(courseImage)

            courseTitle.text = course.name
            coursePrice.text = "$${course.price}"
            courseDuration.text = "${course.videos?.size ?: 0} lessons"
            aboutDescription.text = course.description
        }
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

    fun onLessonCompleted(lessonId: String?) {
        // Проверяем, не был ли урок уже завершен
        if (lessonId != null && !completedLessons.contains(lessonId)) {
            completedLessons.add(lessonId)
            updateProgressUI()
            
            // Сохраняем прогресс в Firestore
            val userId = auth.currentUser?.uid ?: return
            val courseId = course.id
            
            db.collection("users")
                .document(userId)
                .collection("course_progress")
                .document(courseId.toString())
                .set(mapOf(
                    "completed_lessons" to completedLessons,
                    "last_updated" to System.currentTimeMillis()
                ))
                .addOnSuccessListener {
                    // Обновляем UI после успешного сохранения
                    updateProgressUI()
                }
                .addOnFailureListener { e ->
                    // В случае ошибки показываем сообщение
                    Toast.makeText(context, "Failed to save progress: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
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