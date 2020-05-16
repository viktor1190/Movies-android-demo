package com.example.android.architecture.blueprints.movies.data.source

import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result

/**
 * Facade interface to the data layer.
 */
interface MoviesRepository {

    suspend fun getMovies(page: Int, sort: MoviesListSortType?, query: String? = null): Result<List<Movie>>

    suspend fun getMovie(movieId: Int): Result<Movie>

    /**
     * Returns true if login was success.
     */
    suspend fun login(email: String): Result<Boolean>
}

enum class MoviesListSortType(val value: String) {
    TITLE_ASC("title.asc"),
    TITLE_DESC("title.desc"),
    DATE_ASC("date.asc"),
    DATE_DESC("date.desc"),
    POPULARITY_ASC("popularity.asc"),
    POPULARITY_DESC("popularity.desc")
}