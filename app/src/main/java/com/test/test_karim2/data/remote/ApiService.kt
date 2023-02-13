package com.test.test_karim2.data.remote

import com.test.test_karim2.data.model.*
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*


interface ApiService {
    /**
     * API FOR GET MOVIE GENRE
     */
    @GET("3/genre/movie/list")
    fun getMovieGenre(
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieGenreResponse>>

    /**
     * API FOR GET MOVIE BY GENRE
     */
    @GET("3/discover/movie")
    fun getMovieByGenre(
        @Query("api_key") api_key: String,
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): Deferred<Response<MovieByGenreResponse>>

    /**
     * API FOR GET MOVIE DETAIL
     */
    @GET("3/movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieDetailResponse>>

    /**
     * API FOR GET MOVIE REVIEW
     */
    @GET("3/movie/{movie_id}/reviews")
    fun getMovieReview(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): Deferred<Response<MovieReviewResponse>>

    /**
     * API FOR GET MOVIE VIDEOS
     */
    @GET("3/movie/{movie_id}/videos")
    fun getMovieVideos(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): Deferred<Response<MovieVideosResponse>>
}


