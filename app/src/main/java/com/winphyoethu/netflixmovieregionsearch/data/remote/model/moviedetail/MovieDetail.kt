package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail

import com.squareup.moshi.Json

data class MovieDetail(
    @Json(name = "imdbposter") val imdbPoster: String?,
    @Json(name = "year") val year: Int?,
    @Json(name = "imdbrating") val imdbRating: String?,
    @Json(name = "avgrating") val avgRating: Double?,
    @Json(name = "vtype") val vType: String?,
    @Json(name = "img") val image: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "nfdate") val nfDate: String?,
    @Json(name = "imdbruntime") val imdbRuntime: String?,
    @Json(name = "imdbgenre") val imdbGenre: String?,
    @Json(name = "matlabel") val matLabel: String?,
    @Json(name = "netflixid") val netflixId: Int?,
    @Json(name = "imdbplot") val imdbPlot: String?,
    @Json(name = "synopsis") val synopsis: String?
)