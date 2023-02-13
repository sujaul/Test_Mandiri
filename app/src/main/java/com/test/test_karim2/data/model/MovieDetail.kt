package com.test.test_karim2.data.model

open class MovieDetail(
        var budget: Int = 0,
        var id: Int = 0,
        var genres: List<MovieGenre> = listOf(),
        var adult: Boolean = false,
        var homepage: String = "",
        var original_language: String = "",
        var original_title: String = "",
        var overview: String = "",
        var popularity: String = "",
        var poster_path: String = "",
        var production_companies: List<MovieProductionCompany> = listOf(),
        var production_countries: List<MovieProductionCountry> = listOf(),
        var release_date: String = "",
        var revenue: String = "",
        var spoken_languages: List<MovieSpokenLanguage> = listOf(),
        var status: String = "",
        var tagline: String = "",
        var title: String = "",
        var vote_average: String = "",
        var vote_count: String = ""
)

data class MovieProductionCompany(
        var id: Int = 0,
        var logo_path: String = "",
        var name: String = "",
        var origin_country: String = "",
)
data class MovieProductionCountry(
        var name: String = ""
)
data class MovieSpokenLanguage(
        var english_name: String = "",
        var name: String = ""
)
