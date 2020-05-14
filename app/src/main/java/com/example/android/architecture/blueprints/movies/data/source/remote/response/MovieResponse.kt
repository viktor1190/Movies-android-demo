package com.example.android.architecture.blueprints.movies.data.source.remote.response

data class MovieResponse(
        val id: Int,
        val title: String,
        val adult: Boolean,
        val image: String,
        val description: String
)