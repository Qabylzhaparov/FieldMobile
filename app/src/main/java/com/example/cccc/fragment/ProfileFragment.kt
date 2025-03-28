package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cccc.MainActivity
import com.example.cccc.R
import com.example.cccc.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        auth = Firebase.auth
        db = FirebaseFirestore.getInstance()
        
        loadUserData()
        setupClickListeners()
    }

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            // Загружаем данные пользователя
            binding.userName.text = user.displayName ?: "User"
            binding.userEmail.text = user.email
            
            // Загружаем статистику
            loadUserStats(user.uid)
        } else {
            // Если пользователь не авторизован, перенаправляем на экран входа
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private fun loadUserStats(userId: String) {
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                // Загружаем количество курсов
                val coursesCount = document.getLong("courses_count") ?: 0
                binding.coursesCount.text = coursesCount.toString()
                
                // Загружаем количество сертификатов
                val certificatesCount = document.getLong("certificates_count") ?: 0
                binding.certificatesCount.text = certificatesCount.toString()
                
                // Загружаем количество часов
                val hoursCount = document.getLong("hours_count") ?: 0
                binding.hoursCount.text = hoursCount.toString()
            }
    }

    private fun setupClickListeners() {
        with(binding) {
            editProfileButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
            
            notificationsButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_notificationsFragment)
            }
            
            paymentButton.setOnClickListener {
                // TODO: Реализовать управление платежными методами
                showToast("Payment Methods clicked")
            }
            
            securityButton.setOnClickListener {
                // TODO: Реализовать настройки безопасности
                showToast("Security clicked")
            }
            
            logoutButton.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun showLogoutConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { _, _ ->
                (requireActivity() as MainActivity).setBottomNavVisibility(false) // Скрываем BottomNavigationView
                auth.signOut()
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }
}
