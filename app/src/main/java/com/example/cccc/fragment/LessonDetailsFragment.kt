package com.example.cccc.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.cccc.databinding.FragmentLessonDetailsBinding
import com.example.cccc.entity.Video
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class LessonDetailsFragment : Fragment() {

    private var _binding: FragmentLessonDetailsBinding? = null
    private val binding get() = _binding!!
    
    private var player: ExoPlayer? = null
    private var video: Video? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLessonDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Получаем видео из аргументов
        video = arguments?.getParcelable(ARG_VIDEO)
        
        setupVideoPlayer()
        setupTest()
    }

    private fun setupVideoPlayer() {
        video?.let { video ->
            binding.lessonTitle.text = video.title
            
            player = ExoPlayer.Builder(requireContext()).build().apply {
                setMediaItem(MediaItem.fromUri(video.url))
                prepare()
            }
            
            binding.videoPlayer.player = player
        }
    }

    private fun setupTest() {
        binding.submitButton.setOnClickListener {
            val selectedOption = when (binding.answerOptions.checkedRadioButtonId) {
                binding.option1.id -> 0
                binding.option2.id -> 1
                binding.option3.id -> 2
                binding.option4.id -> 3
                else -> -1
            }

            if (selectedOption == -1) {
                Toast.makeText(requireContext(), "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Здесь можно добавить проверку ответа
            Toast.makeText(requireContext(), "Answer submitted!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        player?.release()
        _binding = null
    }

    companion object {
        private const val ARG_VIDEO = "video"
        
        fun newInstance(video: Video) = LessonDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_VIDEO, video)
            }
        }
    }
} 