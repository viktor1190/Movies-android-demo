package com.example.android.architecture.blueprints.movies.data

data class Movie(
        val id: Int,
        val title: String,
        val adultContent: Boolean,
        val image: String?,
        val description: String,
        val tagLine: String? = null,
        val releaseDate: String? = null,
        val status: String? = null,
        val reviews: List<String>? = null,
        val cast: Casting? = null
)

data class Casting (
        val actor: String,
        val character: String,
        val image: String?
)