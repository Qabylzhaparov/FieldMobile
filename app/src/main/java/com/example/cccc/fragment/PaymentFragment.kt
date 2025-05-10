package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.cccc.R
import com.example.cccc.database.CourseRepositoryLocal
import com.example.cccc.databinding.FragmentPaymentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!

    private lateinit var webView: WebView
    private val paymentUrl = "https://buy.stripe.com/test_7sY4gB0IK6a7a2G5lcd3i00"

    private var courseId: Int = -1
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        courseId = arguments?.getInt("courseId") ?: -1
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
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
        
        // Update local database
        CourseRepositoryLocal.getCourses().find { it.id == courseId }?.isPurchased = true

        // Save purchase state to Firestore
        val userId = auth.currentUser?.uid ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.collection("users")
                    .document(userId)
                    .collection("purchased_courses")
                    .document(courseId.toString())
                    .set(mapOf(
                        "courseId" to courseId,
                        "purchaseDate" to System.currentTimeMillis()
                    ))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Set fragment result
        setFragmentResult("payment_result", Bundle().apply {
            putBoolean("unlockLessons", true)
        })

        _binding = null
    }
}
