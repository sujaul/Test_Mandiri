package com.test.test_karim2.data.model

data class MovieGenreResponse(
        var status_code: Int = 0,
        var status_message: String = "",
        var genres: List<MovieGenre> = listOf()
)
