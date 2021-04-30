package com.winphyoethu.netflixmovieregionsearch.data.remote.model.genre

import com.squareup.moshi.Json

data class Genre(
    @Json(name = "genre") val genre: String,
    @Json(name = "netflixid") val netflixId: Int
)