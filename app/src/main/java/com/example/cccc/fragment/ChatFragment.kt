package com.example.cccc.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cccc.adapter.MessageAdapter
import com.example.cccc.databinding.FragmentChatBinding
import com.example.cccc.model.Message
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
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
    private lateinit var generativeModel: GenerativeModel



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

        // Добавляем приветственное сообщение
        messages.add(Message(
            "Hello! I'm your AI assistant. How can I help you today?",
            false
        ))
        messageAdapter.submitList(messages.toList())
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }
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
                // Добавляем сообщение пользователя
                val userMessage = Message(messageText, true)
                messages.add(userMessage)
                messageAdapter.submitList(messages.toList())

                // Очищаем поле ввода
                binding.messageInput.text?.clear()

                // Прокручиваем к последнему сообщению
                binding.messagesRecyclerView.scrollToPosition(messages.size - 1)

                // Получаем ответ от AI
                getAIResponse(messageText)
            }
        }
    }



    private fun getAIResponse(userMessage: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiKey = "AIzaSyBpRcrcvZPH7f1orljH5o9BN2J8I28ZVg0" // Replace with your actual API key
                val url =
                    "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$apiKey"

                val jsonRequest = JSONObject().apply {
                    put("contents", JSONArray().apply {
                        put(JSONObject().apply {
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", "You are AI assistant in my learnhub(online courses app). Act like you are assistant and dont long. Write in plain text without formatting, without asterisks, underlines, or other symbols. Here is message: $userMessage")
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
                    Log.e("ChatFragment", "Error getting AI response: ${e.message}")
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

        println("Request URL: $urlString")
        println("Request JSON: $jsonInput")

        connection.outputStream.use { outputStream ->
            outputStream.write(jsonInput.toByteArray(Charsets.UTF_8))
        }

        val responseCode = connection.responseCode
        println("HTTP Response Code: $responseCode")

        return if (responseCode == 200) {
            connection.inputStream.bufferedReader().use { it.readText() }
        } else {
            val errorResponse = connection.errorStream?.bufferedReader()?.use { it.readText() }
            println("HTTP Error Response: $errorResponse")
            throw Exception("API Error: $responseCode - $errorResponse")
        }
    }


    private fun handleAIResponse(responseJson: String) {
        val jsonObject = JSONObject(responseJson)
        val responseText = jsonObject.optJSONArray("candidates")
            ?.optJSONObject(0)
            ?.optJSONObject("content")
            ?.optJSONArray("parts")
            ?.optJSONObject(0)
            ?.optString("text") ?: "Sorry, I couldn't generate a response."

        messages.add(Message(responseText, false))
        messageAdapter.submitList(messages.toList())
        binding.messagesRecyclerView.scrollToPosition(messages.size - 1)
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}