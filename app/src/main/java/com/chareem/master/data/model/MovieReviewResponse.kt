package com.chareem.master.data.model

data class MovieReviewResponse(
        var status_code: Int = 0,
        var status_message: String = "",
        var results: List<MovieReview> = arrayListOf()
)
