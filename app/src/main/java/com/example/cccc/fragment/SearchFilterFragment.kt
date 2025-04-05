package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cccc.databinding.FragmentSearchFilterBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SearchFilterFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSearchFilterBinding? = null
    private val binding get() = _binding!!

    private var filterAppliedListener: ((Pair<Float, Float>) -> Unit)? = null

    // Сохраняем диапазон цен в переменной
    private var currentPriceRange: Pair<Float, Float>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получаем переданный диапазон цен
        currentPriceRange = arguments?.let {
            val start = it.getFloat("priceStart", 0f)
            val end = it.getFloat("priceEnd", 200f)
            Pair(start, end)
        } ?: Pair(0f, 200f)

        setupListeners()
        setupRangeSlider()
    }

    private fun setupListeners() {
        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.clearButton.setOnClickListener {
            // Сброс всех фильтров
            binding.categoriesChipGroup.clearCheck()
            binding.priceRangeSlider.values = listOf(0f, 200f)
        }

        binding.applyButton.setOnClickListener {
            // Получаем выбранный диапазон цен
            val priceRange = binding.priceRangeSlider.values.let {
                Pair(it[0], it[1])
            }

            // Вызываем колбэк с выбранными фильтрами
            filterAppliedListener?.invoke(priceRange)
            dismiss()
        }
    }

    private fun setupRangeSlider() {
        // Инициализируем слайдер с последними выбранными значениями, если они есть
        val startPrice = currentPriceRange?.first ?: 0f
        val endPrice = currentPriceRange?.second ?: 200f

        binding.priceRangeSlider.apply {
            values = listOf(startPrice, endPrice)
            addOnChangeListener { slider, _, _ ->
                binding.priceStart.text = "$${slider.values[0].toInt()}"
                binding.priceEnd.text = "$${slider.values[1].toInt()}"
            }
        }
    }

    fun setFilterAppliedListener(listener: (Pair<Float, Float>) -> Unit) {
        filterAppliedListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SearchFilterFragment"

        // Новый метод для создания фрагмента с аргументами
        fun newInstance(priceRange: Pair<Float, Float>? = null): SearchFilterFragment {
            val fragment = SearchFilterFragment()
            val args = Bundle()
            priceRange?.let {
                args.putFloat("priceStart", it.first)
                args.putFloat("priceEnd", it.second)
            }
            fragment.arguments = args
            return fragment
        }
    }
}
