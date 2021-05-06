package com.winphyoethu.netflixmovieregionsearch.features.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.CountryDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.MovieDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.SeasonModel
import com.winphyoethu.netflixmovieregionsearch.domain.mapper.MovieMapper
import com.winphyoethu.netflixmovieregionsearch.domain.mapper.SeasonEpisodeMapper
import com.winphyoethu.netflixmovieregionsearch.domain.repository.EpisodeRepository
import com.winphyoethu.netflixmovieregionsearch.domain.repository.MovieRepository
import com.winphyoethu.netflixmovieregionsearch.domain.repository.RemoteRepository
import com.winphyoethu.netflixmovieregionsearch.features.main.ui.savedmovie.SavedMovieViewModel
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    val remoteRepository: RemoteRepository, val movieRepository: MovieRepository, val async: Async,
    val episodeRepository: EpisodeRepository
) : ViewModel() {

    private val TAG = SavedMovieViewModel::class.java.name

    val movieDetailSubject: PublishSubject<MovieDetailState> = PublishSubject.create()

    private val compositeDisposable = CompositeDisposable()

    private lateinit var movieDetail: MovieDetail
    private lateinit var movie: Movie
    private var netflixId: Int = 0
    private var isSaved: Boolean = false
    private val countryList: MutableList<CountryDetail> = ArrayList()
    private val seasonList: MutableList<SeasonModel> = ArrayList()

    fun getMovieDetail(netflixId: Int, isFromSaved: Boolean) {
        this.netflixId = netflixId
        this.isSaved = isFromSaved

        if (this.isSaved) {
            getMovieFromDatabase(netflixId)
        } else {
            checkMovie(netflixId)
        }
    }

    private fun getMovieFromDatabase(netflixId: Int) {
        compositeDisposable.add(
            movieRepository.getMovieAndAvailableCountry(netflixId)
                .map {
                    this.movie = it.movie

                    val movieMap: HashMap<Int, Any> = HashMap()
                    movieMap[0] = MovieMapper.movieToMovieDetail(it.movie)
                    movieMap[1] = MovieMapper.availableCountryToCountryDetail(it.countryList)
                    it.episodeList?.let { episodeList ->
                        movieMap[2] = SeasonEpisodeMapper.episodeToSeasonModel(episodeList)
                    }

                    movieMap
                }
                .subscribeOn(async.io())
                .delay(100, TimeUnit.MILLISECONDS)
                .observeOn(async.main())
                .subscribe({
                    movieDetailSubject.onNext(ShowMovieDetail(it[0] as MovieDetail))
                    movieDetailSubject.onNext(ShowCountries(it[1] as List<CountryDetail>))
                    movieDetailSubject.onNext(CheckSavedMovie(true))
                    if (it[2] != null) {
                        val seasonModelList = it[2] as List<SeasonModel>
                        seasonList.addAll(seasonModelList)
                        movieDetailSubject.onNext(ShowSeasons(seasonList))
                    }
                }, {
                    Log.i(TAG, it.message.toString() + " :: GET MOVIE FROM DB ERROR")
                })
        )
    }

    private fun getMovieFromNetwork(netflixId: Int) {
        compositeDisposable.add(
            remoteRepository.getMovieDetail(netflixId)
                .subscribeOn(async.io())
                .subscribe({
                    movieDetail = it.movieList[0]

                    movieDetailSubject.onNext(ShowMovieDetail(it.movieList[0]))

                    if (movieDetail.vType == "series") {
                        getSeasonAndEpisodes(netflixId)
                    }
                }, {
                    val message = if (it is HttpException) {
                        when (it.code()) {
                            404 -> "Not Found"
                            500 -> "Server Error"
                            else -> "No Internet Connection"
                        }
                    } else {
                        "No Internet Connection"
                    }
                    movieDetailSubject.onNext(ShowError(message, false))
                })
        )
    }

    private fun getMovieCountries(netflixId: Int) {
        compositeDisposable.add(
            remoteRepository.getCountriesDetail(netflixId)
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    countryList.addAll(it.countryList)

                    movieDetailSubject.onNext(ShowCountries(it.countryList))
                }, {
                    Log.i(TAG, "$it GET MOVIE COUNTRY ERROR")
                })
        )
    }

    fun saveMovie() {
        compositeDisposable.add(
            Maybe.just(movieDetail)
                .map {
                    MovieMapper.movieDetailToMovie(it)
                }
                .flatMap {
                    movieRepository.saveMovie(it)
                }
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    it?.let {
                        if (it > 0) {
                            movieDetailSubject.onNext(ShowIsSavedMovie(true))
                        }
                    }
                }, {
                    Log.i(TAG, "$it SAVE MOVIE ERROR")
                })
        )

        compositeDisposable.add(
            Maybe.just(countryList)
                .map {
                    val aCountryList: MutableList<AvailableCountry> = ArrayList()

                    it.forEach { country ->
                        aCountryList.add(MovieMapper.countryDetailToAvailableCountry(country, netflixId))
                    }

                    aCountryList
                }
                .flatMap {
                    movieRepository.saveAvailableCountry(it)
                }
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    Log.i(TAG, "$it SAVE AVAILABLE COUNTRY SUCCESS")
                }, {
                    Log.i(TAG, "$it SAVE AVAILABLE COUNTRY ERROR")
                })
        )

        saveEpisodes(seasonList)
    }

    private fun saveEpisodes(seasonList: List<SeasonModel>) {
        val episodeList: MutableList<Episode> = ArrayList()
        seasonList.forEach { season ->
            season.episodeList.forEach { episode ->
                episodeList.add(SeasonEpisodeMapper.episodeModelToEpisode(episode, season, netflixId))
            }
        }

        compositeDisposable.add(
            episodeRepository.saveEpisodes(episodeList)
                .subscribeOn(async.io())
                .observeOn(async.io())
                .subscribe({
                    Log.i(TAG, "$it SAVE EPISODE SUCCESS")
                }, {
                    Log.i(TAG, it.message.toString() + " SAVE EPISODE ERROR")
                })
        )
    }

    fun deleteMovie() {
        compositeDisposable.add(
            movieRepository.deleteMovie(movie)
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    movieDetailSubject.onNext(ShowIsSavedMovie(false))
                }, {
                    Log.i(TAG, it.message.toString() + " DELETE MOVIE ERROR")
                })
        )
    }

    private fun checkMovie(netflixId: Int) {
        compositeDisposable.add(
            movieRepository.checkMovie(netflixId)
                .subscribeOn(async.io())
                .subscribe({
                    if (it != null) {
                        if (it > 0) {
                            getMovieFromDatabase(netflixId)
                            movieDetailSubject.onNext(CheckSavedMovie(true))
                        } else {
                            movieDetailSubject.onNext(CheckSavedMovie(false))
                            getMovieFromNetwork(netflixId)
                            getMovieCountries(netflixId)
                        }
                    } else {
                        getMovieFromNetwork(netflixId)
                        getMovieCountries(netflixId)
                        movieDetailSubject.onNext(CheckSavedMovie(false))
                    }
                }, {
                    getMovieFromNetwork(netflixId)
                    getMovieCountries(netflixId)
                    movieDetailSubject.onNext(CheckSavedMovie(false))
                })
        )
    }

    private fun getSeasonAndEpisodes(netflixId: Int) {
        compositeDisposable.add(
            remoteRepository.getSeasonAndEpisode(netflixId)
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    seasonList.addAll(it)

                    movieDetailSubject.onNext(ShowSeasons(it))
                }, {

                })
        )
    }

    fun getEpisode(position: Int) {
        compositeDisposable.add(
            Observable.just(seasonList[position].episodeList)
                .observeOn(async.main())
                .subscribe({
                    movieDetailSubject.onNext(ShowEpisodes(it))
                }, {
                    Log.i(TAG, it.message.toString() + " GET EPISODE ERROR")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}