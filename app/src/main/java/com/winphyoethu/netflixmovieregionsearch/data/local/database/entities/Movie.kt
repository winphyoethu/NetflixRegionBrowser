package com.winphyoethu.netflixmovieregionsearch.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Movie(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "netflix_id") val netflixId: Int?,
    @ColumnInfo(name = "title") val title: String?,
    @ColumnInfo(name = "rating") val rating: String?,
    @ColumnInfo(name = "video_type") val videoType: String?,
    @ColumnInfo(name = "synopsis") val synopsis: String?,
    @ColumnInfo(name = "plot") val plot: String?,
    @ColumnInfo(name = "poster") val poster: String?,
    @ColumnInfo(name = "img") val img: String?,
    @ColumnInfo(name = "runtime") val runtime: String?,
    @ColumnInfo(name = "genre") val genre: String?,
    @ColumnInfo(name = "year") val year: Int?,
    @ColumnInfo(name = "maturity_label") val maturityLabel: String?
)