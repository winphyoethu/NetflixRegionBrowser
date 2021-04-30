package com.winphyoethu.netflixmovieregionsearch.data.remote.model.country

import com.squareup.moshi.Json

data class AllCountryRemote(
    @Json(name = "elapse") val elapse: Double?,
    @Json(name = "results") val countryList: List<CountryRemote>
)