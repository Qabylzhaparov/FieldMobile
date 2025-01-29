package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cccc.Course
import com.example.cccc.R
import com.example.cccc.databinding.FragmentCourseDetailsBinding
import com.example.cccc.db.CourseDao
import com.example.cccc.db.CourseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CourseDetailFragment : Fragment() {

    private lateinit var binding: FragmentCourseDetailsBinding
    private lateinit var CourseDao: CourseDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)

        // Получение контакта из аргументов
        val Course = arguments?.getParcelable<Course>("Course")
        Course?.let {
            binding.detailName.text = it.name
            binding.detailShortInfo.text = it.slug
            binding.detailInfo.text = it.description
        }

        // Получаем экземпляр CourseDao
        CourseDao = CourseDatabase.getInstance(requireContext()).CourseDao()

        // Обработка нажатия кнопки "Редактировать"
        binding.buttonEditCourse.setOnClickListener {
            val fragment = CourseCreateFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("Course", Course)
                }
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        // Обработка нажатия кнопки "Удалить"
        binding.buttonDeleteCourse.setOnClickListener {
            Course?.let {
                deleteCourse(it)
            }
        }

        return binding.root
    }

    private fun deleteCourse(Course: Course) {
        // Удаление контакта через корутины
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                CourseDao.deleteCourse(Course)
            }
            // Показываем сообщение и возвращаемся к предыдущему экрану
            Toast.makeText(requireContext(), "Контакт удален", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}
