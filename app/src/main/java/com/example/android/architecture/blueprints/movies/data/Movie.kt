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
        val reviews: List<Review>? = null,
        val cast: List<Casting>? = null
)

data class Review (
        val author: String,
        val value: String
)

data class Casting (
        val actor: String?,
        val character: String?,
        val image: String?
) {
    val description: String
        get() = actor + if (character.isNullOrEmpty()) "" else "\nas $character"
}