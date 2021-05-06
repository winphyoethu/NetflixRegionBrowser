package com.winphyoethu.netflixmovieregionsearch.di.module

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import com.winphyoethu.netflixmovieregionsearch.data.local.database.MovieDatabase
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.AvailableCountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.CountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.EpisodeDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.MovieDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(application: Application): MovieDatabase {
        return Room.databaseBuilder(application, MovieDatabase::class.java, "movie")
            .build()
    }

    @Provides
    fun providesCountryDao(movieDatabase: MovieDatabase): CountryDao {
        return movieDatabase.getCountryDao()
    }

    @Provides
    fun providesMovieDao(movieDatabase: MovieDatabase) : MovieDao {
        return movieDatabase.getMovieDao()
    }

    @Provides
    fun providesAvailableCountryDao(movieDatabase: MovieDatabase): AvailableCountryDao {
        return movieDatabase.getAvailableCountryDao()
    }

    @Provides
    fun providesEpisodeDao(movieDatabase: MovieDatabase): EpisodeDao {
        return movieDatabase.getEpisodeDao()
    }

}