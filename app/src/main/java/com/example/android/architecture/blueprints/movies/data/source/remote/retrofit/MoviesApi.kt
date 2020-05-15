package com.example.android.architecture.blueprints.movies.data.source.remote.retrofit

import com.example.android.architecture.blueprints.movies.data.Result
import com.example.android.architecture.blueprints.movies.data.source.remote.response.KeyResponse
import com.example.android.architecture.blueprints.movies.data.source.remote.response.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {

    /**
     * Obtain the API Key needed for further requests
     * @param email The email to obtain a key
     */
    @GET("/$API_KEY_REQUEST_PATH_SEGMENT")
    suspend fun getKey(@Query("email") email: String): Result<KeyResponse>

    /**
     * Obtain a list of movies with their basic information.
     * @param page The requested page
     * @param sort The property to sort with. Allowed values:
     *      title.asc, title.desc, date.asc, date.desc, popularity.asc, popularity.desc.
     *      If a query is provided the sort value will be ignored.
     * @param query A query to match against a movie title
     */
    @GET("/movies")
    suspend fun getMovies(
            @Query("page") page: Int,
            @Query("sort") sort: String?,
            @Query("query") query: String?
    ): Result<List<MovieResponse>>

    /**
     * Obtain further details for a movie with the id
     * @param id The movie id
     */
    @GET("/movies/{id}")
    suspend fun getMovie(@Path("id") id: Int): Result<MovieResponse>
}