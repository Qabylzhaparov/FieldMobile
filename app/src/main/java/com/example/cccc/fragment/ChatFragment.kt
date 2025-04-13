package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.R
import com.example.cccc.adapter.MessageAdapter
import com.example.cccc.databinding.FragmentChatBinding
import com.example.cccc.model.Message
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private lateinit var messageAdapter: MessageAdapter
    private val messages = mutableListOf<Message>()

    companion object {
        private const val PREFS_NAME = "ChatPrefs"
        private const val KEY_MESSAGES = "messages"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupRecyclerView()
        setupMessageInput()
        loadMessages()
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_clear_chat) {
                clearChat()
                true
            } else false
        }

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.chat_menu, menu)
    }

    private fun setupRecyclerView() {
        messageAdapter = MessageAdapter()
        binding.messagesRecyclerView.apply {
            adapter = messageAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupMessageInput() {
        binding.sendButton.setOnClickListener {
            val messageText = binding.messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                messages.add(Message(messageText, true))
                updateChat()
                binding.messageInput.text?.clear()
                getAIResponse(messageText)
            }
        }
    }

    private fun loadMessages() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, 0)
        val messagesJson = prefs.getString(KEY_MESSAGES, null)

        if (messagesJson != null) {
            val type = object : TypeToken<List<Message>>() {}.type
            messages.addAll(Gson().fromJson(messagesJson, type))
        } else {
            messages.add(Message("Hello! I'm your AI assistant. How can I help you today?", false))
        }

        updateChat()
    }

    private fun saveMessages() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, 0)
        prefs.edit().putString(KEY_MESSAGES, Gson().toJson(messages)).apply()
    }

    private fun clearChat() {
        messages.clear()
        messages.add(Message("Hello! I'm your AI assistant. How can I help you today?", false))
        updateChat()
    }

    private fun updateChat() {
        messageAdapter.submitList(messages.toList())
        binding.messagesRecyclerView.scrollToPosition(messages.size - 1)
        saveMessages()
    }

    private fun getAIResponse(userMessage: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=AIzaSyBpRcrcvZPH7f1orljH5o9BN2J8I28ZVg0"

                // Создаем историю сообщений для контекста
                val conversationHistory = messages.joinToString("\n") { 
                    "${if (it.isFromUser) "User" else "Assistant"}: ${it.text}"
                }

                val jsonRequest = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", """
                                        You are AI assistant in my learnhub(online courses app). 
                                        Act like you are assistant and dont be too long. 
                                        Write in plain text without formatting, without asterisks, underlines, or other symbols.
                                        
                                        Here is our conversation history:
                                        $conversationHistory
                                        
                                        Here is the latest message:
                                        $userMessage
                                    """.trimIndent())
                                })
                            })
                        })
                    })
                }

                val responseText = sendPostRequest(url, jsonRequest.toString())

                withContext(Dispatchers.Main) {
                    handleAIResponse(responseText)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error getting AI response: ${e.message}")
                }
            }
        }
    }

    private fun sendPostRequest(urlString: String, jsonInput: String): String {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        connection.outputStream.use { it.write(jsonInput.toByteArray(Charsets.UTF_8)) }

        return if (connection.responseCode == 200) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            throw Exception("API Error: ${connection.responseCode}")
        }
    }

    private fun handleAIResponse(responseJson: String) {
        val responseText = JSONObject(responseJson).optJSONArray("candidates")
            ?.optJSONObject(0)
            ?.optJSONObject("content")
            ?.optJSONArray("parts")
            ?.optJSONObject(0)
            ?.optString("text") ?: "Sorry, I couldn't generate a response."

        messages.add(Message(responseText, false))
        updateChat()
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}