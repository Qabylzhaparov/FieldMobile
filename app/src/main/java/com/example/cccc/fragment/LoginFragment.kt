package com.example.cccc.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cccc.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private var listener: LoginListener? = null

    // Интерфейс для общения с активити
    interface LoginListener {
        fun onLoginSuccess()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LoginListener) {
            listener = context
        } else {
            throw RuntimeException("$context должен реализовать LoginListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Обработка нажатия кнопки логина
        binding.loginButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            if (password == "5444") {  // Укажите ваш пароль
                listener?.onLoginSuccess()  // Уведомляем MainActivity о успешном логине
            } else {
                Toast.makeText(requireContext(), "Неверный пароль", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}
