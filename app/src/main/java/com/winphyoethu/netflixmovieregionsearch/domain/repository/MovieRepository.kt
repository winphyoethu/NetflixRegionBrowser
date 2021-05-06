package com.winphyoethu.netflixmovieregionsearch.domain.repository

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.MovieAndCountry
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface MovieRepository {

    fun saveMovie(movie: Movie): Maybe<Long>

    fun getMovieList(): Single<List<Movie>>

    fun checkMovie(netflixId: Int): Single<Int?>

    fun saveAvailableCountry(availableCountryList: List<AvailableCountry>): Maybe<List<Long>>

    fun getMovieAndAvailableCountry(netflixId: Int): Single<MovieAndCountry>

    fun deleteMovie(movie: Movie): Completable

}