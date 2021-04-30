package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json

data class CountryDetail(
    @Json(name = "uhd") val uhd: String?,
    @Json(name = "expiredate") val expireDate: String?,
    @Json(name = "subtitle") val subtitle: String?,
    @Json(name = "cc") val cc: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "newdate") val newDate: String?,
    @Json(name = "audio") val audio: String?
)