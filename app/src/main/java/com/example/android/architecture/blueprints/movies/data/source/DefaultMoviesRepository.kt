package com.example.android.architecture.blueprints.movies.data.source

import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.di.ApplicationModule.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultMoviesRepository @Inject constructor(
        @RemoteDataSource private val moviesRemoteDataSource: MoviesRepository,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MoviesRepository {

    override suspend fun getMovies(page: Int, sort: MoviesListSortType?, query: String?): Result<List<Movie>> = withContext(ioDispatcher) {
        return@withContext moviesRemoteDataSource.getMovies(page, sort, query)
    }

    override suspend fun getMovie(movieId: Int): Result<Movie> = withContext(ioDispatcher) {
        return@withContext moviesRemoteDataSource.getMovie(movieId)
    }

    override suspend fun login(email: String): Result<Boolean> = withContext(ioDispatcher) {
        return@withContext moviesRemoteDataSource.login(email)
    }
}
