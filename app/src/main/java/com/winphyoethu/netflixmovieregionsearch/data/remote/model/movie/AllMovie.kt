package com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie

import com.squareup.moshi.Json

data class AllMovie(
    @Json(name = "total") val total: Int?,
    @Json(name = "elapse") val elapse: Double?,
    @Json(name = "results") val movieList: List<MovieRemote>?
)