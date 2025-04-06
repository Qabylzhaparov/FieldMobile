package com.example.cccc.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.R
import com.example.cccc.adapter.CourseAdapter
import com.example.cccc.databinding.FragmentCoursesBinding
import com.example.cccc.database.CourseRepositoryLocal
import com.example.cccc.entity.Course
import com.example.cccc.model.CourseCategory
import com.example.cccc.model.CourseFilter
import com.google.android.material.card.MaterialCardView
import com.google.firebase.firestore.FirebaseFirestore

class CoursesFragment : Fragment() {

    private var _binding: FragmentCoursesBinding? = null
    private val binding get() = _binding!!
    private lateinit var courseAdapter: CourseAdapter
    private lateinit var db: FirebaseFirestore
    
    private var currentFilter = CourseFilter.ALL
    private var currentCategory: CourseCategory? = null
    private var currentSearchQuery: String = ""
    private var allCourses = listOf<Course>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCoursesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        db = FirebaseFirestore.getInstance()
        
        setupRecyclerView()
        setupFilterButtons()
        setupCategoryButtons()
        setupSearch()
        setupFilterDialog()
//        loadCourses()
    }

    private fun setupRecyclerView() {
        courseAdapter = CourseAdapter { course ->
            openCourseDetails(course)
        }

        binding.recyclerViewCourses.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = courseAdapter
        }

        allCourses = CourseRepositoryLocal.getCourses()
        applyFilters()
    }


    private fun setupFilterButtons() {
        val buttons = listOf(
            binding.btnAll to CourseFilter.ALL,
            binding.btnPopular to CourseFilter.POPULAR,
            binding.btnNew to CourseFilter.NEW
        )

        buttons.forEach { (button, filter) ->
            button.setOnClickListener {
                updateFilterButtons(filter)
                applyFilters()
            }
        }
    }

    private fun setupCategoryButtons() {
        val categoryViews = listOf(
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryLanguage) to CourseCategory.MOBILE,
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryPainting) to CourseCategory.BACKEND,
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryMusic) to CourseCategory.DATA_ANALYSIS,
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryPhotography) to CourseCategory.WEB_DEVELOPMENT
        )

        categoryViews.forEach { (view, category) ->
            view.setOnClickListener {
                updateCategorySelection(category)
                applyFilters()
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                currentSearchQuery = s?.toString()?.trim() ?: ""
                applyFilters()
            }
        })
    }

    private fun setupFilterDialog() {
        binding.ivFilter.setOnClickListener {
            val filterFragment = SearchFilterFragment()
            filterFragment.setFilterAppliedListener { priceRange ->
                // Применяем дополнительные фильтры
                applyAdvancedFilters(priceRange)
            }
            filterFragment.show(childFragmentManager, SearchFilterFragment.TAG)
        }
    }

//    private fun loadCourses() {
//        db.collection("courses")
//            .orderBy("createdAt", Query.Direction.DESCENDING)
//            .get()
//            .addOnSuccessListener { documents ->
//                allCourses = documents.mapNotNull { doc ->
//                    doc.toObject(Course::class.java)
//                }
//                applyFilters()
//            }
//    }

    private fun updateFilterButtons(selectedFilter: CourseFilter) {
        currentFilter = selectedFilter
        
        val buttons = listOf(
            binding.btnAll,
            binding.btnPopular,
            binding.btnNew
        )

        buttons.forEach { button ->
            val isSelected = when (selectedFilter) {
                CourseFilter.ALL -> button == binding.btnAll
                CourseFilter.POPULAR -> button == binding.btnPopular
                CourseFilter.NEW -> button == binding.btnNew
            }

            button.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isSelected) R.color.blue else R.color.gray
                )
            )
            button.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    if (isSelected) R.color.white else R.color.gray
                )
            )
        }
    }

    private fun updateCategorySelection(category: CourseCategory) {
        currentCategory = if (currentCategory == category) null else category
        
        // Обновляем визуальное состояние категорий
        val categoryViews = listOf(
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryLanguage),
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryPainting),
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryMusic),
            binding.categoriesScroll.getChildAt(0).findViewById<CardView>(R.id.categoryPhotography)
        )

        categoryViews.forEach { view ->
            (view as? MaterialCardView)?.strokeWidth = if (view.tag == category.name && currentCategory == category) {
                2
            } else {
                0
            }
        }

    }

    private fun applyFilters() {
        var filteredCourses = allCourses

        // Применяем фильтр категории
        if (currentCategory != null) {
            filteredCourses = filteredCourses.filter { it.category == currentCategory }
        }

        // Применяем основной фильтр
        filteredCourses = when (currentFilter) {
            CourseFilter.ALL -> filteredCourses
            CourseFilter.POPULAR -> filteredCourses.sortedByDescending { it.isPopular}
            CourseFilter.NEW -> filteredCourses.sortedByDescending { it.isNew }
        }

        // Применяем поисковый запрос
        if (currentSearchQuery.isNotEmpty()) {
            filteredCourses = filteredCourses.filter {
                it.name.contains(currentSearchQuery, ignoreCase = true) ||
                it.description.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        courseAdapter.submitList(filteredCourses)
    }

    private fun applyAdvancedFilters(priceRange: Pair<Float, Float>) {
        var filteredCourses = allCourses

        // Применяем фильтр по цене
        filteredCourses = filteredCourses.filter {
            it.price in priceRange.first..priceRange.second
        }

        // Применяем остальные фильтры
        if (currentCategory != null) {
            filteredCourses = filteredCourses.filter { it.category == currentCategory }
        }

        filteredCourses = when (currentFilter) {
            CourseFilter.ALL -> filteredCourses
            CourseFilter.POPULAR -> filteredCourses.sortedByDescending { it.isPopular }
            CourseFilter.NEW -> filteredCourses.sortedByDescending { it.isNew }
        }

        if (currentSearchQuery.isNotEmpty()) {
            filteredCourses = filteredCourses.filter {
                it.name.contains(currentSearchQuery, ignoreCase = true) ||
                it.description.contains(currentSearchQuery, ignoreCase = true)
            }
        }

        courseAdapter.submitList(filteredCourses)
    }

    private fun openCourseDetails(course: Course) {
        val bundle = Bundle().apply {
            putParcelable("course", course)
        }
        findNavController().navigate(R.id.action_courses_to_courseDetails, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
