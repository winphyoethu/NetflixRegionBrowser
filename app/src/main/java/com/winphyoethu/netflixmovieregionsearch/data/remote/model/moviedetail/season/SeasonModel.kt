package com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season

import com.squareup.moshi.Json

data class SeasonModel(
    @Json(name = "season") val season: Int,
    @Json(name = "episodes") val episodeList: MutableList<EpisodeModel>
)