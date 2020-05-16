package com.example.android.architecture.blueprints.movies.data.source.remote.response

data class MovieResponse(
        val id: Int,
        val title: String,
        val adult: Boolean,
        val image: String?,
        val description: String,
        // Following attributes are only retrieved from the movie details endpoint
        val tagline: String?,
        val release_date: String?,
        val status: String?,
        val reviews: List<String>?,
        val cast: List<CastResponse>?
)

data class CastResponse (
        val actor: String,
        val character: String,
        val image: String?
)