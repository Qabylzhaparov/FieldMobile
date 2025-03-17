package com.example.cccc.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.cccc.R
import com.example.cccc.databinding.ItemCourseBinding
import com.example.cccc.entity.Course

class CourseAdapter(
    private val onCourseClick: (Course) -> Unit
) : ListAdapter<Course, CourseAdapter.CourseViewHolder>(CourseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val binding = ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = getItem(position)
        holder.bind(course)
    }

    inner class CourseViewHolder(private val binding: ItemCourseBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val course = getItem(adapterPosition)
                onCourseClick(course)
            }
        }

        fun bind(course: Course) {
            binding.courseName.text = course.name
            binding.courseCategory.text = course.category
            binding.coursePrice.text = "$${course.price}$"
            
            Log.d("CourseAdapter", "Loading image for course ${course.name}: ${course.imageUrl}")
            
            Glide.with(binding.root.context)
                .load(course.imageUrl)
                .placeholder(R.drawable.placeholder)
        }
    }
}

class CourseDiffCallback : DiffUtil.ItemCallback<Course>() {
    override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
        return oldItem == newItem
    }
}
