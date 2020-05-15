package com.example.android.architecture.blueprints.movies.di

import androidx.lifecycle.ViewModel
import com.example.android.architecture.blueprints.movies.movieDetail.MovieDetailFragment
import com.example.android.architecture.blueprints.movies.movieDetail.MovieDetailViewModel
import com.example.android.architecture.blueprints.movies.movies.MoviesListFragment
import com.example.android.architecture.blueprints.movies.movies.MoviesListViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class MoviesListModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun moviesListFragment(): MoviesListFragment

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun movieDetailFragment(): MovieDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(MoviesListViewModel::class)
    abstract fun bindMoviesListViewModel(viewModel: MoviesListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailsViewModel(viewModel: MovieDetailViewModel): ViewModel

}
