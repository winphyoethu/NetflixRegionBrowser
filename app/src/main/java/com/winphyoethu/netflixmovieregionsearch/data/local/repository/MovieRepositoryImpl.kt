package com.winphyoethu.netflixmovieregionsearch.data.local.repository

import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.AvailableCountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.MovieDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.MovieAndCountry
import com.winphyoethu.netflixmovieregionsearch.domain.repository.MovieRepository
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(val movieDao: MovieDao, val availableCountryDao: AvailableCountryDao) :
    MovieRepository {

    override fun saveMovie(movie: Movie): Maybe<Long> {
        return movieDao.saveMovie(movie)
    }

    override fun getMovieList(): Single<List<Movie>> {
        return movieDao.getMovieList()
    }

    override fun checkMovie(netflixId: Int): Single<Int?> {
        return movieDao.checkMovie(netflixId)
    }

    override fun saveAvailableCountry(availableCountryList: List<AvailableCountry>): Maybe<List<Long>> {
        return availableCountryDao.saveAvailableCountry(availableCountryList)
    }

    override fun getMovieAndAvailableCountry(netflixId: Int): Single<MovieAndCountry> {
        return movieDao.getMovieAndAvailableCountry(netflixId)
    }

    override fun deleteMovie(movie: Movie): Completable {
        return movieDao.deleteMovie(movie)
    }

}