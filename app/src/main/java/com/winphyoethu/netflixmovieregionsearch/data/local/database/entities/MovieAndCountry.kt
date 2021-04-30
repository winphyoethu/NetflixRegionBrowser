package com.winphyoethu.netflixmovieregionsearch.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class MovieAndCountry(
    @Embedded val movie: Movie,
    @Relation(
        parentColumn = "netflix_id",
        entityColumn = "netflix_id"
    )
    val countryList: List<AvailableCountry>,
    @Relation(
        parentColumn = "netflix_id",
        entityColumn = "netflix_id"
    )
    val episodeList: List<Episode>?
)