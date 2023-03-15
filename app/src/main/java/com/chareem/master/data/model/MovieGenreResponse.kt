package com.chareem.master.data.model

data class MovieGenreResponse(
        var status_code: Int = 0,
        var status_message: String = "",
        var genres: List<MovieGenre> = listOf()
)
