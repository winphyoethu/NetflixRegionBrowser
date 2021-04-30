package com.winphyoethu.netflixmovieregionsearch.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(
    tableName = "available_country",
    foreignKeys = [ForeignKey(
        entity = Movie::class,
        parentColumns = ["netflix_id"],
        childColumns = ["netflix_id"],
        onDelete = CASCADE
    )]
)
data class AvailableCountry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "netflix_id") val netflixId: Int,
    @ColumnInfo(name = "subtitle") val subtitle: String?,
    @ColumnInfo(name = "country_code") val countryCode: String?,
    @ColumnInfo(name = "country") val country: String?,
    @ColumnInfo(name = "audio") val audio: String?
)