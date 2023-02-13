package com.test.test_karim2.data.model

data class MovieVideosResponse(
        var status_code: Int = 0,
        var status_message: String = "",
        var id: Int = 0,
        var results: List<MovieVideos> = arrayListOf()
)
