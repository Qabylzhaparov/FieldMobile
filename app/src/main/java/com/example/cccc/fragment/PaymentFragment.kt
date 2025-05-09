package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cccc.R
import com.example.cccc.database.CourseRepositoryLocal
import com.example.cccc.databinding.FragmentPaymentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var webView: WebView
    private val paymentUrl = "https://buy.stripe.com/test_7sY4gB0IK6a7a2G5lcd3i00"

    private var courseId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        courseId = arguments?.getInt("courseId") ?: -1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)
        webView = binding.webView
        setupWebView()
        return binding.root
    }


    private fun setupWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (url.contains("success")) {
                    Toast.makeText(requireContext(), "Оплата успешна", Toast.LENGTH_SHORT).show()
                } else if (url.contains("cancel")) {
                    Toast.makeText(requireContext(), "Оплата отменена", Toast.LENGTH_SHORT).show()
                }
            }
        }

        webView.loadUrl(paymentUrl)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNav.visibility = View.VISIBLE
        Toast.makeText(requireContext(), "Благодарим за покупку", Toast.LENGTH_SHORT).show()
        CourseRepositoryLocal.getCourses().find { it.id == courseId }?.isPurchased = true

        _binding = null
    }

}
