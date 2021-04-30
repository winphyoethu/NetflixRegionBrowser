package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json

data class AllMovieDetail(
    @Json(name = "elapse") val elapse: Double,
    @Json(name = "results") val movieList: List<MovieDetail>
)