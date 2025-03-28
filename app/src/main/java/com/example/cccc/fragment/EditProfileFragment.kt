package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cccc.databinding.FragmentEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        
        loadUserData()
        setupClickListeners()
    }

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            binding.nameInput.setText(user.displayName ?: "")
            binding.emailInput.setText(user.email ?: "")
        }
    }

    private fun setupClickListeners() {
        binding.saveButton.setOnClickListener {
            updateProfile()
        }
    }

    private fun updateProfile() {
        val newName = binding.nameInput.text.toString()
        val newEmail = binding.emailInput.text.toString()
        
        if (newName.isBlank() || newEmail.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user = auth.currentUser
        if (user != null) {
            // Обновляем имя пользователя
            user.updateProfile(com.google.firebase.auth.UserProfileChangeRequest.Builder()
                .setDisplayName(newName)
                .build())
                .addOnSuccessListener {
                    // Обновляем email
                    user.updateEmail(newEmail)
                        .addOnSuccessListener {
                            // Обновляем данные в Firestore
                            db.collection("users")
                                .document(user.uid)
                                .update(mapOf(
                                    "name" to newName,
                                    "email" to newEmail,
                                    "updated_at" to System.currentTimeMillis()
                                ))
                                .addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                    findNavController().navigateUp()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(requireContext(), "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Failed to update email: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Failed to update name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 