package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json

data class AllCountryDetail(
    @Json(name = "elapse") val elapse: Double,
    @Json(name = "total") val total: Int,
    @Json(name = "results") val countryList: List<CountryDetail>
)