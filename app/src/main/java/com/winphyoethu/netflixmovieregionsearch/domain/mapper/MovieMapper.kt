package com.winphyoethu.netflixmovieregionsearch.domain.mapper

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.CountryDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.MovieDetail

object MovieMapper {

    fun movieToMovieRemote(movie: Movie): MovieRemote {
        return MovieRemote(
            id = null, title = movie.title, img = movie.img, videoType = movie.videoType, netflixId = movie.netflixId,
            synopsis = movie.synopsis, imdbRating = movie.rating, year = movie.year, poster = movie.poster,
            titleDate = null, cList = null
        )
    }

    fun movieDetailToMovie(movieDetail: MovieDetail): Movie {
        return Movie(
            title = movieDetail.title, rating = movieDetail.imdbRating, videoType = movieDetail.vType,
            synopsis = movieDetail.synopsis, plot = movieDetail.imdbPlot, poster = movieDetail.imdbPoster,
            img = movieDetail.image, netflixId = movieDetail.netflixId, runtime = movieDetail.imdbRuntime,
            genre = movieDetail.imdbGenre, year = movieDetail.year, maturityLabel = movieDetail.matLabel
        )
    }

    fun movieToMovieDetail(movie: Movie): MovieDetail {
        return MovieDetail(
            title = movie.title, imdbRating = movie.rating, vType = movie.videoType, synopsis = movie.synopsis,
            imdbPlot = movie.plot, imdbPoster = movie.poster, image = movie.img, netflixId = movie.netflixId,
            imdbRuntime = movie.runtime, imdbGenre = movie.genre, year = movie.year, avgRating = null, nfDate = null,
            matLabel = movie.maturityLabel
        )
    }

    fun countryDetailToAvailableCountry(countryDetail: CountryDetail, netflixId: Int): AvailableCountry {
        return AvailableCountry(
            netflixId = netflixId, subtitle = countryDetail.subtitle, countryCode = countryDetail.cc,
            country = countryDetail.country, audio = countryDetail.audio
        )
    }

    fun availableCountryToCountryDetail(availableCountryList: List<AvailableCountry>): List<CountryDetail> {
        val countryDetailList: MutableList<CountryDetail> = ArrayList()

        availableCountryList.forEach {
            val countryDetail = CountryDetail(
                subtitle = it.subtitle, cc = it.countryCode, country = it.country, audio = it.audio,
                newDate = null, uhd = null, expireDate = null
            )
            countryDetailList.add(countryDetail)
        }

        return countryDetailList
    }

}