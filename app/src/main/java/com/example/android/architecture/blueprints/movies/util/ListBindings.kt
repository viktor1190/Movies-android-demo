package com.example.android.architecture.blueprints.movies.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.movies.adapters.MoviesListAdapter

/**
 * [BindingAdapter]s for the [Movie]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: PagedList<Movie>?) {
    items?.let {
        (listView.adapter as MoviesListAdapter).submitList(it)
    }
}

@BindingAdapter("app:profileImage")
fun loadImage(view: ImageView, imageName: String?) {
    if (imageName != null) {
        Glide.with(view.context)
                .load(MovieImageBuilder.getImageUrl(imageName, MovieImageSize.Medium))
                .into(view)
    }
}

enum class MovieImageSize(val value: String) {
    Minimum("w92"),
    ExtraSmall("w154"),
    Small("w185"),
    Medium("w342"),
    Large("w500"),
    ExtraLarge("w780"),
    Original("original")
}

object MovieImageBuilder {

    fun getImageUrl(imageName: String?, size: MovieImageSize = MovieImageSize.Original): String {
        // TODO improve this logic to use an URL builder together with Retrofit instance to get the base URL
        return "https://dev-candidates.wifiesta.com/images$imageName?size=${size.value}"
    }
}