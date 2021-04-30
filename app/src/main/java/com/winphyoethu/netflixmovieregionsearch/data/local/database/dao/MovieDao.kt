package com.winphyoethu.netflixmovieregionsearch.data.local.database.dao

import androidx.room.*
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.MovieAndCountry
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: Movie): Maybe<Long>

    @Query("SELECT * FROM movie WHERE netflix_id = :netflixId")
    fun getMovie(netflixId: Int): Observable<Movie>

    @Query("SELECT * FROM movie")
    fun getMovieList(): Single<List<Movie>>

    @Transaction
    @Query("SELECT * FROM movie WHERE netflix_id =:netflixId")
    fun getMovieAndAvailableCountry(netflixId: Int): Single<MovieAndCountry>

    @Query("SELECT netflix_id FROM movie WHERE netflix_id =:netflixId")
    fun checkMovie(netflixId: Int): Single<Int?>

    @Delete
    fun deleteMovie(movie: Movie): Completable

}