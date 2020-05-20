package com.example.android.architecture.blueprints.movies.movies

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.movies.LiveDataTestUtil
import com.example.android.architecture.blueprints.movies.MainCoroutineRule
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.source.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoviesListViewModelTest {

    // SUT
    private lateinit var moviesListViewModel: MoviesListViewModel

    // fake repository
    private lateinit var moviesRepository: FakeRepository

    // set the main coroutines dispatcher for unit testing
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        moviesRepository = FakeRepository()
        val movie1 = Movie(1, "Title1", false, "img", "desc")
        val movie2 = Movie(2, "Title2", false, "img", "desc")
        val movie3 = Movie(3, "Title3", false, "img", "desc")
        moviesRepository.addMovies(movie1, movie2, movie3)

        moviesListViewModel = MoviesListViewModel(moviesRepository)

    }

    @Test
    fun `When viewModel is initialized, Should return the first items from the repository`() {
        mainCoroutineRule.runBlockingTest {
            moviesListViewModel = MoviesListViewModel(moviesRepository)
            assertThat(LiveDataTestUtil.getValue(moviesListViewModel.moviesPagedList)).hasSize(3)
        }
    }

}