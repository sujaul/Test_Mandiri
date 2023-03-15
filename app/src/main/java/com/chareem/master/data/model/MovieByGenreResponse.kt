package com.chareem.master.data.model

data class MovieByGenreResponse(
        var status_code: Int = 0,
        var status_message: String = "",
        var results: List<MovieByGenre> = listOf()
)
