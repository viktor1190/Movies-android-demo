package com.example.android.architecture.blueprints.movies.movies

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import com.example.android.architecture.blueprints.movies.data.succeeded
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

const val PAGE_SIZE = 20
const val FIRST_PAGE = 1

class PagedMoviesDataSource constructor(
        private val moviesRepository: MoviesRepository,
        private val coroutineScope: CoroutineScope,
        private val requestStatusObserver: MutableLiveData<Result<List<Movie>>>,
        private val ioDispatcher: CoroutineDispatcher
): PageKeyedDataSource<Int, Movie>() {

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Movie>) {
        managedMoviesRequest(FIRST_PAGE, params.requestedLoadSize) {
            callback.onResult(it, null, FIRST_PAGE + 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        managedMoviesRequest(params.key, params.requestedLoadSize) {
            // incrementing the next page number
            val key = params.key + 1
            callback.onResult(it, key)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        managedMoviesRequest(params.key, params.requestedLoadSize) {
            //if the current page is greater than one we are decrementing the page number else there is no previous page
            val key = if (params.key > 1) params.key - 1 else null
            callback.onResult(it, key)
        }
    }

    private fun managedMoviesRequest(page: Int, pageSize: Int, callback: (List<Movie>) -> Unit) {
        coroutineScope.launch {
            requestStatusObserver.value = Result.Loading
            val moviesResult = withContext(ioDispatcher) { moviesRepository.getMovies(page) }
            if (moviesResult.succeeded) {
                val movies = (moviesResult as Result.Success).data
                requestStatusObserver.value = Result.Success(movies)
                callback(movies)
            } else {
                requestStatusObserver.value = (moviesResult as Result.Error).copy()
            }
        }
    }
}

class MoviesRemoteDataSourceFactory(
        private val moviesRepository: MoviesRepository,
        private val coroutineScope: CoroutineScope,
        private val requestStatusObserver: MutableLiveData<Result<List<Movie>>>,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DataSource.Factory<Int, Movie>() {

    private val moviesLiveDataSource: MutableLiveData<PageKeyedDataSource<Int, Movie>> = MutableLiveData()

    override fun create(): DataSource<Int, Movie> {
        val dataSource = PagedMoviesDataSource(moviesRepository, coroutineScope, requestStatusObserver, ioDispatcher)

        moviesLiveDataSource.postValue(dataSource)

        return dataSource
    }

    fun getMovieLiveDataSource() = moviesLiveDataSource
}