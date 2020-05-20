package com.example.android.architecture.blueprints.movies.data.source

import androidx.annotation.VisibleForTesting
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result

class FakeRepository: MoviesRepository {

    var moviesServiceData: LinkedHashMap<Int, Movie> = LinkedHashMap()

    // Simulates the error message from the service. Set it null to avoid throwing errors
    private var shouldReturnErrorMessage: String? = null

    override suspend fun getMovies(page: Int, sort: MoviesListSortType?, query: String?): Result<List<Movie>> {
        if (shouldReturnErrorMessage != null) {
            return Result.Error(Exception("Expected test exception"), shouldReturnErrorMessage)
        }
        return Result.Success(moviesServiceData.values.toList())
    }

    override suspend fun getMovie(movieId: Int): Result<Movie> {
        if (shouldReturnErrorMessage != null) {
            return Result.Error(Exception("Expected test exception"), shouldReturnErrorMessage)
        }
        val movie = moviesServiceData[movieId]
        return if (movie != null) {
            Result.Success(movie)
        } else {
            Result.Error(Exception("The movie is not present in the testing data-set"), "Movie not found")
        }
    }

    override suspend fun login(email: String): Result<Boolean> {
        if (shouldReturnErrorMessage != null) {
            return Result.Error(Exception("Expected test exception"), shouldReturnErrorMessage)
        }
        return Result.Success(true)
    }

    @VisibleForTesting
    fun addMovies(vararg movies: Movie) {
        for (movie in movies) {
            moviesServiceData[movie.id] = movie
        }
    }
}