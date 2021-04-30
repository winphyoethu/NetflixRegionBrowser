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

interface RemoteRepository {

    fun getCountries(): Single<AllCountryRemote>

    fun getMovieList(
        newDate: String, genre: String, type: String, startYear: String, orderBy: String,
        audioSubtitleAndOr: String, startRating: String, limit: String, endRating: String,
        subtitle: String, countryList: String, query: String, audio: String,
        countryAndOrUnique: String, offset: String, endYear: String,
    ): Single<AllMovie>

    fun getGenres(): Single<AllGenre>

    fun getMovieDetail(netflixId: Int): Single<AllMovieDetail>

    fun getMovieImages(netflixId: Int, offset: Int, limit: Int): Single<AllImageDetail>

    fun getGenresDetail(netflixId: Int): Single<AllGenreDetail>

    fun getCountriesDetail(netflixId: Int): Single<AllCountryDetail>

    fun getSeasonAndEpisode(netflixId: Int): Single<List<SeasonModel>>

}