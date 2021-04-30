package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.genre.Genre

data class AllGenreDetail(
    @Json(name = "elapse") val elapse: Double,
    @Json(name = "results") val genreList: List<GenreDetail>
)