package com.example.android.architecture.blueprints.movies.data.source.remote.retrofit

import com.example.android.architecture.blueprints.movies.data.Casting
import com.example.android.architecture.blueprints.movies.data.Movie
import com.example.android.architecture.blueprints.movies.data.Review
import com.example.android.architecture.blueprints.movies.data.source.remote.response.CastResponse
import com.example.android.architecture.blueprints.movies.data.source.remote.response.MovieResponse
import com.example.android.architecture.blueprints.movies.data.source.remote.response.ReviewResponse
import javax.inject.Inject

interface ResponseDataMapper<in R, out T> {

    fun mapToModel(response: R): T
}

class MovieResponseDataMapper @Inject constructor(
        private val castMapper: CastResponseDataMapper,
        private val reviewMapper: ReviewResponseDataMapper
) : ResponseDataMapper<MovieResponse, Movie> {

    override fun mapToModel(response: MovieResponse): Movie {
        return Movie(response.id,
                response.title,
                response.adult,
                response.image,
                response.description,
                if (response.tagline.isNullOrBlank()) null else response.tagline,
                response.release_date,
                response.status,
                response.reviews?.map { reviewMapper.mapToModel(it) },
                response.cast?.map { castMapper.mapToModel(it) })
    }
}

class CastResponseDataMapper @Inject constructor() : ResponseDataMapper<CastResponse, Casting> {

    override fun mapToModel(response: CastResponse): Casting {
        return Casting(response.actor, response.character, response.image)
    }
}

class ReviewResponseDataMapper @Inject constructor() : ResponseDataMapper<ReviewResponse, Review> {

    override fun mapToModel(response: ReviewResponse): Review {
        return Review(response.author, response.value)
    }
}