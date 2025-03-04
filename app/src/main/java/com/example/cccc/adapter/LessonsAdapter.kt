package com.example.cccc.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cccc.R
import com.example.cccc.databinding.ItemLessonBinding
import com.example.cccc.model.Lesson

class LessonsAdapter(
    private val onLessonClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonsAdapter.LessonViewHolder>() {

    private val lessons = mutableListOf<Lesson>()

    fun setLessons(newLessons: List<Lesson>) {
        lessons.clear()
        lessons.addAll(newLessons)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val binding = ItemLessonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LessonViewHolder(binding, onLessonClick)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(lessons[position])
    }

    override fun getItemCount(): Int = lessons.size

    class LessonViewHolder(
        private val binding: ItemLessonBinding,
        private val onLessonClick: (Lesson) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(lesson: Lesson) {
            with(binding) {
                lessonNumber.text = lesson.number
                lessonTitle.text = lesson.title
                lessonDuration.text = lesson.duration
                
                lessonStatus.setImageResource(
                    if (lesson.isLocked) R.drawable.ic_lock else R.drawable.ic_play
                )
                lessonStatus.setColorFilter(
                    if (lesson.isLocked) 0xFFBDBDBD.toInt() else 0xFF4C6FFF.toInt()
                )

                root.setOnClickListener {
                    onLessonClick(lesson)
                }
            }
        }
    }
} 