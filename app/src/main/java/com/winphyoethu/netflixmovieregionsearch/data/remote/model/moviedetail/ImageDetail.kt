package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json

data class ImageDetail(
    @Json(name = "url") val url: String,
    @Json(name = "itype") val imageType: String,
    @Json(name = "netflixid") val netflixId: String
)