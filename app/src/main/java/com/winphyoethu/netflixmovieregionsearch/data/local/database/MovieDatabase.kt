package com.winphyoethu.netflixmovieregionsearch.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.AvailableCountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.CountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.EpisodeDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.MovieDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Country
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie

@Database(
    entities = [Country::class, Movie::class, AvailableCountry::class, Episode::class],
    version = 1,
    exportSchema = true
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun getCountryDao(): CountryDao

    abstract fun getMovieDao(): MovieDao

    abstract fun getAvailableCountryDao(): AvailableCountryDao

    abstract fun getEpisodeDao(): EpisodeDao

}