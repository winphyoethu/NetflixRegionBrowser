package com.winphyoethu.netflixmovieregionsearch.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "episode",
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = ["netflix_id"],
        childColumns = ["netflix_id"],
        onDelete = CASCADE
    )]
)
data class Episode(
    @PrimaryKey
    @ColumnInfo(name = "episode_id") val episodeId: Int,
    @ColumnInfo(name = "season_id") val seasonId: Int,
    @ColumnInfo(name = "episode_title") val episodeTitle: String?,
    @ColumnInfo(name = "synopsis") val synopsis: String?,
    @ColumnInfo(name = "netflix_id") val netflixId: Int?
)