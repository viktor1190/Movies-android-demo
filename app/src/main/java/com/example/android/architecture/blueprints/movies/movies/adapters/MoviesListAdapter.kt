package com.example.android.architecture.blueprints.movies.movies.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.databinding.MovieItemBinding
import com.example.android.architecture.blueprints.movies.movies.MoviesListViewModel
import com.example.android.architecture.blueprints.movies.movies.adapters.MoviesListAdapter.ViewHolder

/**
 * Adapter for the paged list. Has a reference to the [MoviesListViewModel] to send actions back to it.
 */
class MoviesListAdapter(private val viewModel: MoviesListViewModel) : PagedListAdapter<Movie, ViewHolder>(ItemDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(viewModel, item)
        } else {
            Toast.makeText(holder.itemView.context, "No more items to show", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: MoviesListViewModel, item: Movie) {
            binding.viewmodel = viewModel
            binding.movie = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
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
class ItemDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}
