package com.example.android.architecture.blueprints.movies.di

import android.content.Context
import com.example.android.architecture.blueprints.movies.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main component for the application.
 *
 */
@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        MoviesListModule::class
    ])
interface ApplicationComponent : AndroidInjector<MyApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
