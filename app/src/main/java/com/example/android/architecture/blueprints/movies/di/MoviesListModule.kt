package com.example.android.architecture.blueprints.movies.di

import androidx.lifecycle.ViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(MoviesListViewModel::class)
    abstract fun bindViewModel(viewModel: MoviesListViewModel): ViewModel
}
