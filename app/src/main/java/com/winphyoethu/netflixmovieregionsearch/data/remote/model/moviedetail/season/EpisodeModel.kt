package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season

import com.squareup.moshi.Json

data class EpisodeModel(
    @Json(name = "netflixid") val netflixId: Int,
    @Json(name = "epid") val episodeId: Int,
    @Json(name = "seasid") val seasonId: Int?,
    @Json(name = "epnum") val episodeNumber: Int?,
    @Json(name = "seasnum") val seasonNumber: Int?,
    @Json(name = "synopsis") val synopsis: String?,
    @Json(name = "title") val title: String?,
    @Json(name = "img") val image: String?
)