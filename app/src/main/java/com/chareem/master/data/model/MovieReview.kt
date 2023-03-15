package com.chareem.master.data.model

data class MovieReview(
        var id: String = "",
        var author: String = "",
        var author_details: AuthorDetail? = null,
        var content: String = "",
        var created_at: String = "",
        var updated_at: String = ""
)

data class AuthorDetail(
        var name: String = "",
        var username: String = "",
        var avatar_path: String = "",
        var rating: String = ""
)
