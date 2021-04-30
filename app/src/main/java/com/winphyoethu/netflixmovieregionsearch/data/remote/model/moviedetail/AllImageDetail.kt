package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json

data class AllImageDetail(
    @Json(name = "elapse") val elapse: Double,
    @Json(name = "total") val total: Int,
    @Json(name = "results") val imageList: List<ImageDetail>
)