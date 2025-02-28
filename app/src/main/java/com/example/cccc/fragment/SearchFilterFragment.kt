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
            binding.durationChipGroup.clearCheck()
            binding.priceRangeSlider.values = listOf(0f, 200f)
        }

        binding.applyButton.setOnClickListener {
            // Здесь будет логика применения фильтров
            dismiss()
        }
    }

    private fun setupRangeSlider() {
        binding.priceRangeSlider.apply {
            values = listOf(90f, 200f)
            addOnChangeListener { slider, _, _ ->
                binding.priceStart.text = "$${slider.values[0].toInt()}"
                binding.priceEnd.text = "$${slider.values[1].toInt()}"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "SearchFilterFragment"
        
        fun newInstance() = SearchFilterFragment()
    }
}