package com.example.cccc.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                uploadImage(uri)
            }
        }
    }

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
        storage = FirebaseStorage.getInstance()
        
        loadUserData()
        setupClickListeners()
    }

    private fun loadUserData() {
        val user = auth.currentUser
        if (user != null) {
            // Загружаем данные пользователя
            binding.userName.text = user.displayName ?: "User"
            binding.userEmail.text = user.email
            
            // Загружаем аватарку
            loadUserAvatar(user.uid)
            
            // Загружаем статистику
            loadUserStats(user.uid)
            
            // Загружаем настройки уведомлений
            loadNotificationSettings(user.uid)
        } else {
            // Если пользователь не авторизован, перенаправляем на экран входа
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }

    private fun loadUserAvatar(userId: String) {
        storage.reference.child("avatars/$userId.jpg")
            .downloadUrl
            .addOnSuccessListener { uri ->
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.userAvatar)
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

    private fun loadNotificationSettings(userId: String) {
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                val settings = document.get("notificationSettings") as? Map<String, Boolean> ?: mapOf()
                binding.notificationsSwitch.isChecked = settings["enabled"] ?: true
            }
    }

    private fun setupClickListeners() {
        with(binding) {
            avatarCard.setOnClickListener {
                openImagePicker()
            }
            
            userAvatar.setOnClickListener {
                openImagePicker()
            }
            
            editProfileButton.setOnClickListener {
                findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
            }
            
            notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
                updateNotificationSettings(isChecked)
            }
            
            paymentButton.setOnClickListener {
                // TODO: Реализовать управление платежными методами
                showToast("Payment Methods clicked")
            }
            
            logoutButton.setOnClickListener {
                showLogoutConfirmationDialog()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }

    private fun uploadImage(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val imageRef = storage.reference.child("avatars/$userId.jpg")
        
        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Обновляем аватарку в UI
                    Glide.with(this)
                        .load(downloadUri)
                        .circleCrop()
                        .into(binding.userAvatar)
                    
                    // Сохраняем URL в Firestore
                    db.collection("users")
                        .document(userId)
                        .update("avatarUrl", downloadUri.toString())
                }
            }
            .addOnFailureListener {
                showToast("Failed to upload image")
            }
    }

    private fun updateNotificationSettings(enabled: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users")
            .document(userId)
            .update("notificationSettings.enabled", enabled)
            .addOnSuccessListener {
                showToast("Notification settings updated")
            }
            .addOnFailureListener {
                showToast("Failed to update notification settings")
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
