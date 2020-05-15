package com.example.android.architecture.blueprints.movies

import com.example.android.architecture.blueprints.movies.di.ApplicationComponent
import com.example.android.architecture.blueprints.movies.di.DaggerApplicationComponent
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * An [Application] that uses Dagger for Dependency Injection.
 *
 * Also, sets up Timber in the DEBUG BuildConfig. Read Timber's documentation for production setups.
 */
open class MyApplication : DaggerApplication() {

    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return component
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
            Stetho.initializeWithDefaults(this)
        }
    }
}
