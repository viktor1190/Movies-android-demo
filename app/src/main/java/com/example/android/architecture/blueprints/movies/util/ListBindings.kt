package com.example.android.architecture.blueprints.movies.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.example.android.architecture.blueprints.movies.data.Casting
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Review
import com.example.android.architecture.blueprints.movies.data.source.remote.retrofit.IMAGES_PATH_SEGMENT
import com.example.android.architecture.blueprints.movies.di.API_BASE_URL
import com.example.android.architecture.blueprints.movies.movieDetail.adapters.CastListAdapter
import com.example.android.architecture.blueprints.movies.movieDetail.adapters.ReviewsListAdapter
import com.example.android.architecture.blueprints.movies.movies.adapters.MoviesListAdapter
import okhttp3.HttpUrl

/**
 * [BindingAdapter]s for the [Movie]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: PagedList<Movie>?) {
    items?.let {
        (listView.adapter as MoviesListAdapter).submitList(it)
    }
}

/**
 * [BindingAdapter]s for the [Casting]s list.
 */
@BindingAdapter("app:actors")
fun setActors(listView: RecyclerView, items: List<Casting>?) {
    items?.let {
        (listView.adapter as CastListAdapter).submitList(it)
    }
}

/**
 * [BindingAdapter]s for the [Casting]s list.
 */
@BindingAdapter("app:reviews")
fun setReviews(listView: RecyclerView, items: List<Review>?) {
    items?.let {
        (listView.adapter as ReviewsListAdapter).submitList(it)
    }
}

@BindingAdapter("app:profileImage", "app:profileSize")
fun loadImage(view: ImageView, profileImage: String?, profileSize: MovieImageSize) {
    GlideApp.with(view.context)
            .load(MovieImageBuilder.getImageUrl(profileImage, profileSize))
            .into(view)

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

    fun getImageUrl(imageName: String?, size: MovieImageSize = MovieImageSize.Original): String? {
        if (imageName == null) {
            return null
        }
        return HttpUrl.get(API_BASE_URL).newBuilder()
                .addPathSegment(IMAGES_PATH_SEGMENT)
                .addPathSegment(imageName)
                .addQueryParameter("size", size.value)
                .build().toString()
    }
}