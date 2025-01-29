package com.example.cccc

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.adapter.CourseAdapter
import com.example.cccc.databinding.ActivityMainBinding
import com.example.cccc.fragment.CourseCreateFragment
import com.example.cccc.fragment.CourseDetailFragment
import com.example.cccc.fragment.LoginFragment
import com.example.cccc.vm.CourseRepository
import com.example.cccc.vm.CourseViewModel
import com.example.cccc.vm.CourseViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity(), LoginFragment.LoginListener {

    @Inject
    lateinit var courseRepository: CourseRepository
    private lateinit var courseViewModel: CourseViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Показываем LoginFragment при запуске
        showLoginFragment()

        // Инициализация ViewModel и RecyclerView после логина
        setupRecyclerViewAndViewModel()
    }

    // Метод для показа LoginFragment
    private fun showLoginFragment() {
        val fragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    // Метод для инициализации RecyclerView и ViewModel
    private fun setupRecyclerViewAndViewModel() {
        courseViewModel = ViewModelProvider(this, CourseViewModelFactory(courseRepository))[CourseViewModel::class.java]

        adapter = CourseAdapter { course ->
            val fragment = CourseDetailFragment().apply {
                arguments = Bundle().apply {
                    putParcelable("Course", course)
                }
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        courseViewModel.allCourses.observe(this, { courses ->
            courses?.let { adapter.submitList(it) }
        })

        binding.addCourseButton.setOnClickListener {
            val fragment = CourseCreateFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }


    // Метод, который будет вызван из LoginFragment после успешного входа
    override fun onLoginSuccess() {
        supportFragmentManager.beginTransaction()
            .remove(supportFragmentManager.findFragmentById(R.id.fragment_container) as Fragment)
            .commit()

        // Показываем основной контент после логина
        binding.recyclerView.visibility = View.VISIBLE
        binding.addCourseButton.visibility = View.VISIBLE
    }
}
