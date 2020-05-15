package com.example.android.architecture.blueprints.movies.di

import android.content.Context
import com.example.android.architecture.blueprints.movies.MyApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.OkHttpClient
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
        LoginModule::class,
        MoviesListModule::class
    ])
interface ApplicationComponent : AndroidInjector<MyApplication> {

    val okHttpClient: OkHttpClient

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}
