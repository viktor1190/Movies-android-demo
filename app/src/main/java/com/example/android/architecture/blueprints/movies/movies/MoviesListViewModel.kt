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
import com.example.android.architecture.blueprints.movies.R
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.source.MoviesListSortType
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import com.example.android.architecture.blueprints.movies.data.succeeded
import com.example.android.architecture.blueprints.movies.movies.adapters.MoviesRemoteDataSourceFactory
import com.example.android.architecture.blueprints.movies.movies.adapters.PAGE_SIZE
import javax.inject.Inject

class MoviesListViewModel @Inject constructor(moviesRepository: MoviesRepository) : ViewModel() {

    val moviesPagedList: LiveData<PagedList<Movie>>

    private val _liveDataSource: MutableLiveData<PageKeyedDataSource<Int, Movie>>

    // Setting the default sort order by TITLE_ASC
    private val _sortType: MutableLiveData<MoviesListSortType> = MutableLiveData(MoviesListSortType.TITLE_ASC)
    val sortTypeLabel: LiveData<Int?> = Transformations.map(_sortType) {
        return@map when (it) {
            MoviesListSortType.TITLE_ASC -> R.string.menu_sortBy_titleAsc_label
            MoviesListSortType.TITLE_DESC -> R.string.menu_sortBy_titleDesc_label
            MoviesListSortType.DATE_ASC -> R.string.menu_sortBy_dateAsc_label
            MoviesListSortType.DATE_DESC -> R.string.menu_sortBy_dateDesc_label
            MoviesListSortType.POPULARITY_ASC -> R.string.menu_sortBy_popularityAsc_label
            MoviesListSortType.POPULARITY_DESC -> R.string.menu_sortBy_popularityDesc_label
            else -> null
        }
    }

    // Initially we don't want to filter any movie, so we send null as default value
    private val filterValue: MutableLiveData<String?> = MutableLiveData(null)

    /**
     * This private LiveData object is used to observe the request status and emmit the errors and the loading indicators.
     */
    private val _moviesResultStatusObserver = MutableLiveData<Result<List<Movie>>>()
    val dataLoading: LiveData<Boolean> = Transformations.map(_moviesResultStatusObserver) { it == Result.Loading }
    private val _errorsMessages: MutableLiveData<String> = MutableLiveData()
    val errorsMessages: LiveData<String> = _errorsMessages

    private val _emptyList: MediatorLiveData<Boolean> = MediatorLiveData()
    val emptyList: LiveData<Boolean> = _emptyList

    private val _openMovieEvent = MutableLiveData<Event<Int>>()
    val openMovieEvent: LiveData<Event<Int>> = _openMovieEvent

    private val _searchSuggestions = MediatorLiveData<Map<Int, String>>()
    val searchSuggestions: LiveData<Map<Int, String>> = _searchSuggestions

    init {
        val dataSourceFactory = MoviesRemoteDataSourceFactory(
                moviesRepository,
                viewModelScope,
                _moviesResultStatusObserver,
                _sortType,
                filterValue)
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
        activateEmptyListPlaceholder()
    }

    /**
     * Called by Data Binding.
     */
    fun openMovie(movieId: Int) {
        _openMovieEvent.value = Event(movieId)
    }

    fun setSortType(sortType: MoviesListSortType) {
        _sortType.value = sortType
        refresh()
    }

    fun filterByName(filterValue: String?) {
        this.filterValue.value = filterValue
        refresh()
    }

    fun refresh() {
        _liveDataSource.value?.invalidate()
        activateEmptyListPlaceholder()
    }

    /**
     * Reports an empty list until new items become shown successfully
     */
    private fun activateEmptyListPlaceholder() {
        if (emptyList.value != true) {
            _emptyList.value = true
            _emptyList.addSource(_moviesResultStatusObserver) {
                if (it is Result.Success && it.succeeded && it.data.isNotEmpty()) {
                    _emptyList.value = false
                    _emptyList.removeSource(_moviesResultStatusObserver)
                }
            }
        }
    }
}
