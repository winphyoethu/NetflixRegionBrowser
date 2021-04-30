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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    RemoteRepository {

    override fun getCountries(): Single<AllCountryRemote> {
        return apiService.getCountries()
    }

    override fun getMovieList(
        newDate: String, genre: String, type: String, startYear: String, orderBy: String,
        audioSubtitleAndOr: String, startRating: String, limit: String, endRating: String,
        subtitle: String, countryList: String, query: String, audio: String,
        countryAndOrUnique: String, offset: String, endYear: String
    ): Single<AllMovie> {
        return apiService.searchMovie(
            newDate, genre, type, startYear, orderBy, audioSubtitleAndOr, startRating, limit,
            endRating, subtitle, countryList, query, audio, countryAndOrUnique, offset, endYear
        )
    }

    override fun getGenres(): Single<AllGenre> {
        return apiService.getGenres()
    }

    override fun getMovieDetail(netflixId: Int): Single<AllMovieDetail> {
        return apiService.getMovieDetail(netflixId)
    }

    override fun getMovieImages(netflixId: Int, offset: Int, limit: Int): Single<AllImageDetail> {
        return apiService.getMovieImages(netflixId, offset, limit)
    }

    override fun getGenresDetail(netflixId: Int): Single<AllGenreDetail> {
        return apiService.getGenresDetail(netflixId)
    }

    override fun getCountriesDetail(netflixId: Int): Single<AllCountryDetail> {
        return apiService.getCountriesDetail(netflixId)
    }

    override fun getSeasonAndEpisode(netflixId: Int): Single<List<SeasonModel>> {
        return apiService.getSeasonAndEpisodes(netflixId)
    }

}