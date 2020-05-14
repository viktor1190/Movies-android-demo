package com.example.android.architecture.blueprints.movies.data

data class Movie(
        val id: Int,
        val title: String,
        val adultContent: Boolean,
        val image: String?,
        val description: String
)