package com.test.test_karim2.data.model

data class MovieReviewResponse(
        var status_code: Int = 0,
        var status_message: String = "",
        var results: List<MovieReview> = arrayListOf()
)
