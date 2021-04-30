package com.winphyoethu.netflixmovieregionsearch.features.main

data class FilterModel(
    var query: String = "",
    var startRating: String = "0",
    var endRating: String = "10",
    var startYear: String = "1900",
    var endYear: String = "2021",
    var movieType: String = "movie",
    var selectedCountries: Int = 0,
    var selectedCountriesList: String = ""
)