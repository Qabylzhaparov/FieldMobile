package com.example.cccc.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.cccc.R
import com.example.cccc.model.Lesson

class LessonAdapter(
    private val onLessonClick: (Lesson) -> Unit
) : ListAdapter<Lesson, LessonAdapter.LessonViewHolder>(LessonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lesson, parent, false)
        return LessonViewHolder(view)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class LessonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val lessonNumber: TextView = itemView.findViewById(R.id.lessonNumber)
        private val lessonTitle: TextView = itemView.findViewById(R.id.lessonTitle)
        private val lessonDuration: TextView = itemView.findViewById(R.id.lessonDuration)

        fun bind(lesson: Lesson) {
            lessonNumber.text = "Lesson ${lesson.number}"
            lessonTitle.text = lesson.title
            lessonDuration.text = lesson.duration

            itemView.setOnClickListener {
                onLessonClick(lesson)
            }
        }
    }

    private class LessonDiffCallback : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }
    }
} 