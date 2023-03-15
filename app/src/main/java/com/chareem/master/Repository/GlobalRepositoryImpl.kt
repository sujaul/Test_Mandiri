package com.chareem.master.Repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.chareem.master.data.local.AppDatabase
import com.chareem.master.data.model.*
import com.chareem.master.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class GlobalRepositoryImpl(
    private val service: ApiService
): globalRepository, KoinComponent {

    private val apiKey = "cd83415e05660af23207154d1df177a7"
    private val db: AppDatabase by inject()

    override suspend fun getGenreLocal(): Flow<List<MovieGenre>> {
        return db.movieGenreDAO().all()
    }

    override suspend fun getGenreServer() {
        val response = service.getMovieGenre(apiKey).await()
        if (response.isSuccessful){
            try {
                response.body()?.let {res ->
                    if (res.status_code == 0){
                        db.movieGenreDAO().deleteAll()
                        db.movieGenreDAO().insertLists(res.genres)
                    } else throw Throwable(res.status_message)
                } ?: throw Throwable("The body is null")
            } catch (e: Exception){
                throw Throwable(e.message)
            }
        } else {
            if (response.errorBody()!=null) {
                val json: String = response.errorBody()!!.string()
                val obj: JsonObject = JsonParser().parse(json).asJsonObject
                val error = Gson().fromJson(obj, MovieGenreResponse::class.java)
                throw Throwable(error.status_message)
            } else {
                throw Throwable("The error body is null")
            }
        }
    }

    override suspend fun getMovieByGenre(genreId: Int, page: Int): List<MovieByGenre> {
        val movie = ArrayList<MovieByGenre>()
        val response = service.getMovieByGenre(apiKey, genreId, page).await()
        if (response.isSuccessful){
            try {
                response.body()?.let {res ->
                    if (res.status_code == 0){
                        movie.addAll(res.results)
                    } else throw Throwable(res.status_message)
                } ?: throw Throwable("The body is null")
            } catch (e: Exception){
                throw Throwable(e.message)
            }
        } else {
            if (response.errorBody()!=null) {
                val json: String = response.errorBody()!!.string()
                val obj: JsonObject = JsonParser().parse(json).asJsonObject
                val error = Gson().fromJson(obj, MovieGenreResponse::class.java)
                throw Throwable(error.status_message)
            } else {
                throw Throwable("The error body is null")
            }
        }
        return movie
    }

    override suspend fun getMovieDetail(movieId: Int): MovieDetail {
        var movieDetail= MovieDetail()
        val response = service.getMovieDetail(movieId, apiKey).await()
        if (response.isSuccessful){
            try {
                response.body()?.let {res ->
                    if (res.status_code == 0){
                        movieDetail = res
                    } else throw Throwable(res.status_message)
                } ?: throw Throwable("The body is null")
            } catch (e: Exception){
                throw Throwable(e.message)
            }
        } else {
            if (response.errorBody()!=null) {
                val json: String = response.errorBody()!!.string()
                val obj: JsonObject = JsonParser().parse(json).asJsonObject
                val error = Gson().fromJson(obj, MovieDetailResponse::class.java)
                throw Throwable(error.status_message)
            } else {
                throw Throwable("The error body is null")
            }
        }
        return movieDetail
    }

    override suspend fun getMovieReview(movieId: Int, page: Int): List<MovieReview> {
        val movieReview = ArrayList<MovieReview>()
        val response = service.getMovieReview(movieId, apiKey, page).await()
        if (response.isSuccessful){
            try {
                response.body()?.let {res ->
                    if (res.status_code == 0){
                        movieReview.addAll(res.results)
                    } else throw Throwable(res.status_message)
                } ?: throw Throwable("The body is null")
            } catch (e: Exception){
                throw Throwable(e.message)
            }
        } else {
            if (response.errorBody()!=null) {
                val json: String = response.errorBody()!!.string()
                val obj: JsonObject = JsonParser().parse(json).asJsonObject
                val error = Gson().fromJson(obj, MovieReviewResponse::class.java)
                throw Throwable(error.status_message)
            } else {
                throw Throwable("The error body is null")
            }
        }
        return movieReview
    }

    override suspend fun getMovieVideos(movieId: Int): MovieVideosResponse {
        var movieVideos = MovieVideosResponse()
        val response = service.getMovieVideos(movieId, apiKey).await()
        if (response.isSuccessful){
            try {
                response.body()?.let {res ->
                    if (res.status_code == 0){
                        movieVideos = res
                    } else throw Throwable(res.status_message)
                } ?: throw Throwable("The body is null")
            } catch (e: Exception){
                throw Throwable(e.message)
            }
        } else {
            if (response.errorBody()!=null) {
                val json: String = response.errorBody()!!.string()
                val obj: JsonObject = JsonParser().parse(json).asJsonObject
                val error = Gson().fromJson(obj, MovieVideosResponse::class.java)
                throw Throwable(error.status_message)
            } else {
                throw Throwable("The error body is null")
            }
        }
        return movieVideos
    }
}

interface globalRepository{
    suspend fun getGenreLocal(): Flow<List<MovieGenre>>
    suspend fun getGenreServer()
    suspend fun getMovieByGenre(genreId: Int, page: Int): List<MovieByGenre>
    suspend fun getMovieDetail(movieId: Int): MovieDetail
    suspend fun getMovieReview(movieId: Int, page: Int): List<MovieReview>
    suspend fun getMovieVideos(movieId: Int): MovieVideosResponse
}