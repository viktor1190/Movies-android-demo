package com.example.android.architecture.blueprints.movies.data.source.remote.retrofit

import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.source.remote.response.MovieResponse
import javax.inject.Inject

interface ResponseDataMapper<in R, out T> {

    fun mapToModel(response: R): T
}

class MovieResponseDataMapper @Inject constructor() : ResponseDataMapper<MovieResponse, Movie> {

    override fun mapToModel(response: MovieResponse): Movie {
        return Movie(response.id, response.title, response.adult, response.image, response.description)
    }
}