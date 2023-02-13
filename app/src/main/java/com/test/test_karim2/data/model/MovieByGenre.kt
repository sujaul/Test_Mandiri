package com.test.test_karim2.data.model

data class MovieByGenre(
        var id: Int = 0,
        var adult: Boolean = false,
        var genre_id: Int = 0,
        var backdrop_path: String = "",
        var original_title: String = "",
        var overview: String = "",
        var popularity: String = "",
        var poster_path: String = "",
        var release_date: String = "",
        var title: String = "",
        var vote_average: String = "",
        var vote_count: String = ""
)
