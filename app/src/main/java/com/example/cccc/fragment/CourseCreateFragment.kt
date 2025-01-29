package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.cccc.Course
import com.example.cccc.vm.CourseViewModel
import com.example.cccc.databinding.FragmentCourseCreateBinding

class CourseCreateFragment : Fragment() {

    private lateinit var courseViewModel: CourseViewModel
    private lateinit var binding: FragmentCourseCreateBinding
    private var course: Course? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCourseCreateBinding.inflate(inflater, container, false)
        courseViewModel = ViewModelProvider(requireActivity()).get(CourseViewModel::class.java)

        // Получаем переданный объект Course
        course = arguments?.getParcelable("Course")
        course?.let {
            binding.nameEditText.setText(it.name)
            binding.shortInfoEditText.setText(it.slug)
            binding.infoEditText.setText(it.description)
        }

        // Обработка нажатия кнопки "Сохранить"
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val slug = binding.shortInfoEditText.text.toString()
            val description = binding.infoEditText.text.toString()

            if (course == null) {
                // Создание нового курса
                val newCourse = Course(name = name, slug = slug, description = description)
                courseViewModel.insert(newCourse)
            } else {
                // Обновление существующего курса
                val updatedCourse = course!!.copy(name = name, slug = slug, description = description)
                courseViewModel.update(updatedCourse)
            }

            parentFragmentManager.popBackStack()
        }

        binding.cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return binding.root
    }
}
