package com.example.android.architecture.blueprints.movies.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.example.android.architecture.blueprints.movies.Event
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import com.example.android.architecture.blueprints.movies.data.succeeded
import com.example.android.architecture.blueprints.movies.movies.adapters.MoviesRemoteDataSourceFactory
import com.example.android.architecture.blueprints.movies.movies.adapters.PAGE_SIZE
import javax.inject.Inject

class MoviesListViewModel @Inject constructor(val moviesRepository: MoviesRepository) : ViewModel() {

    val moviesPagedList: LiveData<PagedList<Movie>>

    private val _liveDataSource: MutableLiveData<PageKeyedDataSource<Int, Movie>>

    /**
     * This private LiveData object is used to observe the request status and emmit the errors and the loading indicators.
     */
    private val _moviesResultStatusObserver = MutableLiveData<Result<List<Movie>>>()
    val dataLoading: LiveData<Boolean> = Transformations.map(_moviesResultStatusObserver) { it == Result.Loading }
    private val _errorsMessages: MutableLiveData<String> = MutableLiveData()
    val errorsMessages: LiveData<String> = _errorsMessages

    val emptyList: LiveData<Boolean> = Transformations.map(_moviesResultStatusObserver) { !it.succeeded }

    private val _openMovieEvent = MutableLiveData<Event<Int>>()
    val openMovieEvent: LiveData<Event<Int>> = _openMovieEvent

    private val _searchSuggestions = MediatorLiveData<Map<Int, String>>()
    val searchSuggestions: LiveData<Map<Int, String>> = _searchSuggestions

    init {
        val dataSourceFactory = MoviesRemoteDataSourceFactory(moviesRepository, viewModelScope, _moviesResultStatusObserver)
        _liveDataSource = dataSourceFactory.getMovieLiveDataSource()
        val pagedListConfig = PagedList.Config.Builder()
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(80)
                .setPageSize(PAGE_SIZE)
                .setPrefetchDistance(3)
                .build()
        moviesPagedList = LivePagedListBuilder(dataSourceFactory, pagedListConfig).build()
        _searchSuggestions.addSource(_moviesResultStatusObserver) {
            if (it.succeeded) {
                val movies = (it as Result.Success).data
                val mapOfMoviesTitles = movies.map { it.id to it.title }.toMap()
                _searchSuggestions.value = mapOfMoviesTitles
            } else if (it is Result.Error) {
                _errorsMessages.value = it.message ?: it.exception.message
            }
        }
    }

    /**
     * Called by Data Binding.
     */
    fun openMovie(movieId: Int) {
        _openMovieEvent.value = Event(movieId)
    }

    fun refresh() {
        _liveDataSource.value?.invalidate()
    }
}
