package com.example.android.architecture.blueprints.movies.di

import androidx.lifecycle.ViewModel
import com.example.android.architecture.blueprints.movies.login.LoginFragment
import com.example.android.architecture.blueprints.movies.login.ui.login.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class LoginModule {

    @ContributesAndroidInjector(modules = [ViewModelBuilder::class])
    internal abstract fun loginFragment(): LoginFragment

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindViewModel(viewModel: LoginViewModel): ViewModel
}