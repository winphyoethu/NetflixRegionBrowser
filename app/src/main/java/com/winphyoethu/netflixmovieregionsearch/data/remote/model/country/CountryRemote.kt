package com.winphyoethu.netflixmovieregionsearch.data.remote.model.country

import com.squareup.moshi.Json

data class CountryRemote(
    @Json(name = "id") val id: Int,
    @Json(name = "country") val country: String,
    @Json(name = "countrycode") val countryCode: String
)