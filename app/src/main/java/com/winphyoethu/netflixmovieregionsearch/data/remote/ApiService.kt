package com.winphyoethu.netflixmovieregionsearch.data.remote

import com.winphyoethu.netflixmovieregionsearch.data.remote.model.country.AllCountryRemote
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.genre.AllGenre
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.AllMovie
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.AllCountryDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.AllGenreDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.AllImageDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.AllMovieDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.SeasonModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search")
    fun searchMovie(
        @Query("newdate") newDate: String,
        @Query("genrelist") genre: String,
        @Query("type") type: String,
        @Query("start_year") startYear: String,
        @Query("orderby") orderBy: String,
        @Query("audiosubtitle_andor") audioSubtitleAndOr: String,
        @Query("start_rating") startRating: String,
        @Query("limit") limit: String,
        @Query("end_rating") endRating: String,
        @Query("subtitle") subtitle: String,
        @Query("countrylist") countryList: String,
        @Query("query") query: String,
        @Query("audio") audio: String,
        @Query("country_andorunique") countryAndOrUnique: String,
        @Query("offset") offset: String,
        @Query("end_year") endYear: String,
    ): Single<AllMovie>

    @GET("countries")
    fun getCountries(): Single<AllCountryRemote>

    @GET("genres")
    fun getGenres(): Single<AllGenre>

    @GET("title")
    fun getMovieDetail(
        @Query("netflixid") netflixId: Int
    ): Single<AllMovieDetail>

    @GET("images")
    fun getMovieImages(
        @Query("netflixid") netflixId: Int,
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Single<AllImageDetail>

    @GET("titlegenres")
    fun getGenresDetail(
        @Query("netflixid") netflixId: Int
    ): Single<AllGenreDetail>

    @GET("titlecountries")
    fun getCountriesDetail(
        @Query("netflixid") netflixId: Int
    ): Single<AllCountryDetail>

    @GET("episodes")
    fun getSeasonAndEpisodes(@Query("netflixid") netflixId: Int): Single<List<SeasonModel>>

}