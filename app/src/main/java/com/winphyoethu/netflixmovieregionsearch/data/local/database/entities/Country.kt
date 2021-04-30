package com.winphyoethu.netflixmovieregionsearch.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "country_code") val countryCode: String,
    @ColumnInfo(name = "country_id") val countryId: Int
)
