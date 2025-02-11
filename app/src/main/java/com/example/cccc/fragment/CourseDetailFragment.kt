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


        return binding.root
    }
}
