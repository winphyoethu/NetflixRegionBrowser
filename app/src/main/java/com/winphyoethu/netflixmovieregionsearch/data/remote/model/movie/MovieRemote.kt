package com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie

import com.squareup.moshi.Json

data class MovieRemote(
    @Json(name = "id") val id: Int?,
    @Json(name = "title") val title: String?,
    @Json(name = "img") val img: String?,
    @Json(name = "vtype") val videoType: String?,
    @Json(name = "nfid") val netflixId: Int?,
    @Json(name = "synopsis") val synopsis: String?,
    @Json(name = "year") val year: Int?,
    @Json(name = "poster") val poster: String?,
    @Json(name = "imdbrating") val imdbRating: String?,
    @Json(name = "clist") val cList: String?,
    @Json(name = "titledate") val titleDate: String?
)