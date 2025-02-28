package com.example.cccc.newfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.adapter.CourseAdapter
import com.example.cccc.databinding.FragmentCoursesBinding
import com.example.cccc.db.CourseRepository

class CoursesFragment : Fragment() {

    private lateinit var binding: FragmentCoursesBinding
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        courseAdapter = CourseAdapter { course ->
            // Здесь можно обработать нажатие на курс
        }

        binding.recyclerViewCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        // Загружаем курсы из репозитория
        courseAdapter.submitList(CourseRepository.getCourses())
    }
}
