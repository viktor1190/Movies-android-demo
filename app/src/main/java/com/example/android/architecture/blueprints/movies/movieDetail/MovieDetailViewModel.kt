package com.example.android.architecture.blueprints.movies.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import com.example.android.architecture.blueprints.movies.data.succeeded
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    private val _movieDetail = MutableLiveData<Movie>()
    val movieDetail: LiveData<Movie> = _movieDetail

    fun start(movieId: Int) {
        // TODO victor.valencia show data loading progress
        viewModelScope.launch {
            val movieResult = moviesRepository.getMovie(movieId)
            if (movieResult.succeeded) {
                _movieDetail.value = (movieResult as Result.Success).data
            } else if (movieResult is Result.Error) {
                Timber.e(movieResult.exception, movieResult.message)
                // TODO victor.valencia pending to show an error to the user
            }
        }
    }
}
