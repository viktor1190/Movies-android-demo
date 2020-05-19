package com.example.android.architecture.blueprints.movies.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.architecture.blueprints.movies.data.Casting
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.Review
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import com.example.android.architecture.blueprints.movies.data.succeeded
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(private val moviesRepository: MoviesRepository): ViewModel() {

    private val _movieDetail = MutableLiveData<Movie>()
    val movieDetail: LiveData<Movie> = _movieDetail

    private val _errors = MutableLiveData<String>()
    val errorMessageHandler: LiveData<String> = _errors

    private val _castList = MutableLiveData<List<Casting>>()
    val castList: LiveData<List<Casting>?> = _castList

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>?> = _reviews

    fun load(movieId: Int) {
        viewModelScope.launch {
            val movieResult = moviesRepository.getMovie(movieId)
            if (movieResult is Result.Success && movieResult.succeeded) {
                _movieDetail.value = movieResult.data
                _castList.value = movieResult.data.cast
                _reviews.value = movieResult.data.reviews
            } else if (movieResult is Result.Error) {
                Timber.e(movieResult.exception, movieResult.message)
                _errors.value = movieResult.message ?: movieResult.exception.message
            }
        }
    }
}
