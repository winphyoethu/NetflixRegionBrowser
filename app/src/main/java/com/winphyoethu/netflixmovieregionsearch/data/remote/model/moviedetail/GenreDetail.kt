package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json

data class GenreDetail(
    @Json(name = "genre") val genre: String,
    @Json(name = "nfid") val netflixId: Int
)