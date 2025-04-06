package com.example.cccc.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cccc.R
import com.example.cccc.adapter.LessonAdapter
import com.example.cccc.databinding.DialogCertificateBinding
import com.example.cccc.databinding.FragmentCourseDetailsBinding
import com.example.cccc.entity.Course
import com.example.cccc.model.Lesson
import com.example.cccc.service.ProgressService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.cccc.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseDetailsFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!
    
    private var isFavorite = false
    private lateinit var lessonAdapter: LessonAdapter
    private lateinit var course: Course
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var appDatabase: AppDatabase
    private lateinit var progressService: ProgressService
    
    private var completedLessons = mutableListOf<String>()
    private var totalLessons = 0
    private var loadProgressJob: Job? = null

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
        appDatabase = AppDatabase.getDatabase(requireContext())
        progressService = ProgressService(appDatabase, db, auth)
        
        course = arguments?.getParcelable("course") ?: throw IllegalStateException("Course is required")
        
        setupToolbar()
        setupRecyclerView()
        loadCourseProgress()
        updateUI()
        setupClickListeners()
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
                        putString("courseId", course.id.toString())
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
                videoUrl = video.url,
                isCompleted = completedLessons.contains(video.id.toString())
            )
        }

        lessonAdapter.submitList(lessonList)
        totalLessons = lessonList.size
    }

    private fun loadCourseProgress() {
        val userId = auth.currentUser?.uid ?: return
        val courseId = course.id
        
        // Отменяем предыдущий job, если он существует
        loadProgressJob?.cancel()
        
        loadProgressJob = lifecycleScope.launch {
            try {
                // Получаем прогресс из ProgressService
                val progress = progressService.getCourseProgress(userId, courseId.toString())
                progress.collect { progresses ->
                    completedLessons.clear()
                    completedLessons.addAll(progresses.filter { it.isCompleted }.map { it.lessonId })
                    
                    updateProgressUI()
                    lessonAdapter.currentList.forEach { lesson ->
                        lesson.isCompleted = completedLessons.contains(lesson.id)
                    }
                    lessonAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                // Игнорируем ошибку, если фрагмент уже уничтожен
                if (isAdded) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun updateProgressUI() {
        val progress = if (totalLessons > 0) {
            (completedLessons.size.toFloat() / totalLessons.toFloat() * 100).toInt()
        } else {
            0
        }
        
        binding.progressIndicator.progress = progress
        binding.progressText.text = "$progress%"
        binding.progressDescription.text = "${completedLessons.size} of $totalLessons lessons completed"

        binding.getCertificateButton.visibility = if (progress >= 100) View.VISIBLE else View.GONE
    }

    private fun updateUI() {
        with(binding) {
            Glide.with(requireContext())
                .load(course.imageUrl)
                .into(courseImage)

            courseTitle.text = course.name
            coursePrice.text = "$${course.price}"
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

            getCertificateButton.setOnClickListener {
                showCertificateDialog()
            }
        }
    }

    private fun showCertificateDialog() {
        val dialogBinding = DialogCertificateBinding.inflate(layoutInflater)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()

        // Генерируем сертификат
        val certificateBitmap = generateCertificate()
        dialogBinding.certificateImage.setImageBitmap(certificateBitmap)

        // Настраиваем кнопки
        dialogBinding.downloadButton.setOnClickListener {
            saveCertificate(certificateBitmap)
        }

        dialog.show()
    }

    private fun generateCertificate(): Bitmap {
        val width = 1200
        val height = 800

        // Загружаем шаблон сертификата из ресурсов
        val background = BitmapFactory.decodeResource(resources, R.drawable.cerf_template3)
        val scaledBackground = Bitmap.createScaledBitmap(background, width, height, true)

        // Создаем новый Bitmap и Canvas для рисования
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Рисуем фон (шаблон сертификата)
        canvas.drawBitmap(scaledBackground, 0f, 0f, null)

        // Настраиваем текст
        val textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 48f
            isAntiAlias = true
            typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        }

        // Рисуем заголовок
        val title = "Certificate of Completion"
        val titleWidth = textPaint.measureText(title)
        canvas.drawText(title, (width - titleWidth) / 2, 270f, textPaint)

        // Рисуем имя пользователя
        textPaint.textSize = 36f
        val userName = auth.currentUser?.displayName ?: "Student"
        val userNameWidth = textPaint.measureText(userName)
        canvas.drawText(userName, (width - userNameWidth) / 2, 350f, textPaint)

        // Рисуем текст о завершении курса
        textPaint.textSize = 24f
        val courseText = "has successfully completed the course"
        val courseTextWidth = textPaint.measureText(courseText)
        canvas.drawText(courseText, (width - courseTextWidth) / 2, 400f, textPaint)

        // Рисуем название курса
        textPaint.textSize = 32f
        val courseNameWidth = textPaint.measureText(course.name)
        canvas.drawText(course.name, (width - courseNameWidth) / 2, 450f, textPaint)

        // Рисуем дату
        textPaint.textSize = 20f
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val date = dateFormat.format(Date())
        val dateWidth = textPaint.measureText(date)
        canvas.drawText(date, (width - dateWidth) / 2, 550f, textPaint)

        return bitmap
    }

    private fun saveCertificate(bitmap: Bitmap) {
        try {
            val fileName = "certificate_${course.name.replace(" ", "_")}_${System.currentTimeMillis()}.png"
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }

            showToast("Certificate saved to Downloads")

            // Обновляем медиа-хранилище, чтобы файл сразу появился в галерее
            MediaScannerConnection.scanFile(
                requireContext(),
                arrayOf(file.absolutePath),
                arrayOf("image/png"),
                null
            )

        } catch (e: Exception) {
            showToast("Failed to save certificate: ${e.message}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Отменяем job при уничтожении view
        loadProgressJob?.cancel()
        loadProgressJob = null
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
