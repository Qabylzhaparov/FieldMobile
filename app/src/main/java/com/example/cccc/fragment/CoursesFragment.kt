package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.R
import com.example.cccc.adapter.CourseAdapter
import com.example.cccc.databinding.FragmentCoursesBinding
import com.example.cccc.db.CourseRepositoryLocal
import com.example.cccc.entity.Course

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

        setupRecyclerView()
        setupFilterButton()
    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter { course ->
            // Открываем детали курса при клике
            openCourseDetails(course)
        }

        binding.recyclerViewCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        // Загружаем курсы из репозитория
        courseAdapter.submitList(CourseRepositoryLocal.getCourses())
    }

    private fun setupFilterButton() {
        binding.ivFilter.setOnClickListener {
            SearchFilterFragment.newInstance().show(
                childFragmentManager,
                SearchFilterFragment.TAG
            )
        }
    }

    private fun openCourseDetails(course: Course) {
        val bundle = Bundle().apply {
            putParcelable("course", course)
        }
        findNavController().navigate(R.id.action_courses_to_courseDetails, bundle)
    }
}
