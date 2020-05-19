package com.example.android.architecture.blueprints.movies.movieDetail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.movies.data.Review
import com.example.android.architecture.blueprints.movies.databinding.ReviewItemBinding

class ReviewsListAdapter: ListAdapter<Review, ReviewsListAdapter.ReviewViewHolder>(ReviewItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ReviewViewHolder(val binding: ReviewItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Review) {
            binding.review = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ReviewViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ReviewItemBinding.inflate(layoutInflater, parent, false)
                return ReviewViewHolder(binding)
            }
        }

    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minimum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class ReviewItemDiffCallback : DiffUtil.ItemCallback<Review>() {

    override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
        return oldItem == newItem
    }
}