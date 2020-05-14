package com.example.android.architecture.blueprints.movies.data.source.remote

import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.source.MoviesListSortType
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import com.example.android.architecture.blueprints.movies.data.source.remote.response.MovieResponse
import com.example.android.architecture.blueprints.movies.data.source.remote.retrofit.MoviesApi
import com.example.android.architecture.blueprints.movies.data.source.remote.retrofit.ResponseDataMapper
import com.example.android.architecture.blueprints.movies.data.succeeded
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MoviesRemoteDataSource @Inject constructor(
        private val moviesApi: MoviesApi,
        private val mapper: ResponseDataMapper<MovieResponse, Movie>
) : MoviesRepository {

    override suspend fun getMovies(page: Int, sort: MoviesListSortType?, query: String?): Result<List<Movie>> {
        val result = moviesApi.getMovies(page, sort?.value, query)
        return mapMoviesListResult(result)
    }

    override suspend fun getMovie(movieId: Int): Result<Movie> {
        val result = moviesApi.getMovie(movieId)
        return mapMovieResult(result)
    }

    override suspend fun login(email: String): Result<Boolean> {
        val result = moviesApi.getKey(email)
        return if(result.succeeded) {
            Result.Success(true)
        } else {
            result as Result.Error
        }
    }

    private fun mapMoviesListResult(result: Result<List<MovieResponse>>): Result<List<Movie>> {
        return if (result.succeeded) {
            val responseData = (result as Result.Success).data
            Result.Success(responseData.map{ mapper.mapToModel(it) })
        } else {
            result as Result.Error
        }
    }

    private fun mapMovieResult(result: Result<MovieResponse>): Result<Movie> {
        return if (result.succeeded) {
            val responseData = (result as Result.Success).data
            Result.Success(mapper.mapToModel(responseData))
        } else {
            result as Result.Error
        }
    }
}
