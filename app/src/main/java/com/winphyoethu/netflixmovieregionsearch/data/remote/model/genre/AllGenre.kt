package com.winphyoethu.netflixmovieregionsearch.data.remote.model.genre

import com.squareup.moshi.Json

data class AllGenre(
    @Json(name = "elapse") val elapse: Double,
    @Json(name = "results") val genreList: List<Genre>
)