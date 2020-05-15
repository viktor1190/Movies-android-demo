package com.example.android.architecture.blueprints.movies.di

import com.example.android.architecture.blueprints.movies.data.source.DefaultMoviesRepository
import com.example.android.architecture.blueprints.movies.data.source.MoviesRepository
import com.example.android.architecture.blueprints.movies.data.source.remote.MoviesRemoteDataSource
import com.example.android.architecture.blueprints.movies.data.source.remote.retrofit.ApikeyInterceptor
import com.example.android.architecture.blueprints.movies.data.source.remote.retrofit.MovieResponseDataMapper
import com.example.android.architecture.blueprints.movies.data.source.remote.retrofit.MoviesApi
import com.example.android.architecture.blueprints.movies.data.source.remote.retrofit.ResultDataCallAdapterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import dagger.Binds
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.RUNTIME


@Module(includes = [ApplicationModuleBinds::class])
object ApplicationModule {

    @Qualifier
    @Retention(RUNTIME)
    annotation class RemoteDataSource

    @Qualifier
    @Retention(RUNTIME)
    annotation class LocalDataSource

    @JvmStatic
    @Singleton
    @RemoteDataSource
    @Provides
    fun provideMoviesRemoteDataSource(moviesApi: MoviesApi, mapper: MovieResponseDataMapper): MoviesRepository {
        return MoviesRemoteDataSource(moviesApi, mapper)
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @JvmStatic
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(ApikeyInterceptor())
                .addNetworkInterceptor(StethoInterceptor())
                .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
                .addCallAdapterFactory(ResultDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://dev-candidates.wifiesta.com/")
                .build()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit) = retrofit.create(MoviesApi::class.java)
}

@Module
abstract class ApplicationModuleBinds {

    @Singleton
    @Binds
    abstract fun bindRepository(repo: DefaultMoviesRepository): MoviesRepository
}
