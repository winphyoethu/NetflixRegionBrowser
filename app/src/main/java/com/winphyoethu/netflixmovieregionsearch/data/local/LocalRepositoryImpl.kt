package com.winphyoethu.netflixmovieregionsearch.data.local

import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.AvailableCountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.CountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.EpisodeDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.MovieDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepositoryImpl @Inject constructor(
    val countryDao: CountryDao, val movieDao: MovieDao,
    val availableCountryDao: AvailableCountryDao, val episodeDao: EpisodeDao
) : LocalRepository {

    override fun getCountries(): Single<List<Country>> {
        return countryDao.getCountry()
    }

    override fun saveCountries(countryList: List<Country>): Maybe<List<Long>> {
        return countryDao.insertCountry(countryList)
    }

    override fun saveMovie(movie: Movie): Maybe<Long> {
        return movieDao.saveMovie(movie)
    }

    override fun getMovie(netflixId: Int): Observable<Movie> {
        return movieDao.getMovie(netflixId)
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

    override fun saveEpisodes(episodeList: List<Episode>): Maybe<List<Long>> {
        return episodeDao.saveEpisodes(episodeList)
    }

}