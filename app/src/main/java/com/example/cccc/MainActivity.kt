package com.example.cccc

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.adapter.CourseAdapter
import com.example.cccc.databinding.ActivityMainBinding
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

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onLoginSuccess() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navController.navigate(R.id.action_loginFragment_to_homeFragment)
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    fun setBottomNavVisibility(isVisible: Boolean) {
        binding.bottomNavigationView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

}
