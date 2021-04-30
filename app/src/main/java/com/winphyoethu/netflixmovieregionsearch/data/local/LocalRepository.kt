package com.winphyoethu.netflixmovieregionsearch.data.local

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.*
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface LocalRepository {

    fun getCountries(): Single<List<Country>>

    fun saveCountries(countryList: List<Country>): Maybe<List<Long>>

    fun saveMovie(movie: Movie): Maybe<Long>

    fun getMovie(netflixId: Int): Observable<Movie>

    fun getMovieList(): Single<List<Movie>>

    fun checkMovie(netflixId: Int): Single<Int?>

    fun saveAvailableCountry(availableCountryList: List<AvailableCountry>): Maybe<List<Long>>

    fun getMovieAndAvailableCountry(netflixId: Int): Single<MovieAndCountry>

    fun deleteMovie(movie: Movie): Completable

    fun saveEpisodes(episodeList: List<Episode>): Maybe<List<Long>>

}