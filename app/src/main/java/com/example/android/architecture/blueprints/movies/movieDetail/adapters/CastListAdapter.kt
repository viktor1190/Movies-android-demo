package com.example.android.architecture.blueprints.movies.movieDetail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.movies.data.Casting
import com.example.android.architecture.blueprints.movies.databinding.CastItemBinding

class CastListAdapter: ListAdapter<Casting, CastListAdapter.CastViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        return CastViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class CastViewHolder(val binding: CastItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Casting) {
            binding.cast = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): CastViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CastItemBinding.inflate(layoutInflater, parent, false)
                return CastViewHolder(binding)
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
class ItemDiffCallback : DiffUtil.ItemCallback<Casting>() {

    override fun areItemsTheSame(oldItem: Casting, newItem: Casting): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Casting, newItem: Casting): Boolean {
        return oldItem == newItem
    }
}