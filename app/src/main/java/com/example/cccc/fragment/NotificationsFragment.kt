package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cccc.R
import com.example.cccc.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        loadNotificationSettings()
        setupClickListeners()
    }

    private fun loadNotificationSettings() {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val settings = document.get("notificationSettings") as? Map<String, Boolean> ?: mapOf()
                binding.courseUpdatesSwitch.isChecked = settings["courseUpdates"] ?: true
                binding.newMessagesSwitch.isChecked = settings["newMessages"] ?: true
                binding.marketingSwitch.isChecked = settings["marketing"] ?: false
            }
    }

    private fun setupClickListeners() {
        binding.courseUpdatesSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateNotificationSetting("courseUpdates", isChecked)
        }

        binding.newMessagesSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateNotificationSetting("newMessages", isChecked)
        }

        binding.marketingSwitch.setOnCheckedChangeListener { _, isChecked ->
            updateNotificationSetting("marketing", isChecked)
        }
    }

    private fun updateNotificationSetting(setting: String, value: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("users").document(userId)
            .update("notificationSettings.$setting", value)
            .addOnSuccessListener {
                showToast("Settings updated successfully")
            }
            .addOnFailureListener {
                showToast("Failed to update settings")
            }
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance() = NotificationsFragment()
    }
} 