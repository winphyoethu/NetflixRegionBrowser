package com.winphyoethu.netflixmovieregionsearch.features.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.room.ColumnInfo
import com.winphyoethu.netflixmovieregionsearch.data.local.LocalRepository
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie
import com.winphyoethu.netflixmovieregionsearch.data.remote.RemoteRepository
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.CountryDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.MovieDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.SeasonModel
import com.winphyoethu.netflixmovieregionsearch.util.mapper.MovieMapper
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    val remoteRepository: RemoteRepository, val localRepository: LocalRepository, val async: Async
) : ViewModel() {

//    val movieSubject: PublishSubject<MovieDetail> = PublishSubject.create()
//    val countrySubject: PublishSubject<List<CountryDetail>> = PublishSubject.create()
//    val isSavedSubject: PublishSubject<Boolean> = PublishSubject.create()
//    val checkSavedSubject: PublishSubject<Boolean> = PublishSubject.create()
//    val seasonSubject: PublishSubject<List<SeasonModel>> = PublishSubject.create()
//    val episodeSubject: PublishSubject<List<EpisodeModel>> = PublishSubject.create()

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
            localRepository.getMovieAndAvailableCountry(netflixId)
                .map {
                    this.movie = it.movie

                    val movieMap: HashMap<Int, Any> = HashMap()
                    movieMap[0] = MovieMapper.movieToMovieDetail(it.movie)
                    movieMap[1] = MovieMapper.availableCountryToCountryDetail(it.countryList)
                    it.episodeList?.let { episodeList ->
                        movieMap[2] = MovieMapper.episodeToSeasonModel(episodeList)
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
//                    movieSubject.onNext(it[0] as MovieDetail)
//                    countrySubject.onNext(it[1] as List<CountryDetail>)
                    if (it[2] != null) {
                        val seasonModelList = it[2] as List<SeasonModel>
                        seasonList.addAll(seasonModelList)
//                        seasonSubject.onNext(seasonList)
                        movieDetailSubject.onNext(ShowSeasons(seasonList))
                    }
                }, {
                    Log.i("CHEKMOVIEERR :: ", it.message.toString() +" :: DICK")
                })
        )
    }

    private fun getMovieFromNetwork(netflixId: Int) {
        compositeDisposable.add(
            remoteRepository.getMovieDetail(netflixId)
                .subscribeOn(async.io())
                .subscribe({
                    movieDetail = it.movieList[0]
//                    movieSubject.onNext(it.movieList[0])

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
//                    countrySubject.onNext(it.countryList)

                    movieDetailSubject.onNext(ShowCountries(it.countryList))
                }, {
                    Log.i("FUCKERR :: ", it.toString())
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
                    localRepository.saveMovie(it)
                }
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    it?.let {
                        if (it > 0) {
//                            isSavedSubject.onNext(true)

                            movieDetailSubject.onNext(ShowIsSavedMovie(true))
                        }
                    }
                }, {
                    Log.i("FUCK :: ", it.toString())
                })
        )

        compositeDisposable.add(
            Maybe.just(countryList)
                .map {
                    val aCountryList: MutableList<AvailableCountry> = ArrayList()

                    it.forEach { country ->
                        aCountryList.add(
                            MovieMapper.countryDetailToAvailableCountry(country, netflixId)
                        )
                    }

                    aCountryList
                }
                .flatMap {
                    localRepository.saveAvailableCountry(it)
                }
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    Log.i("FUCKERROR :: ", it.toString() + " YES ")
                }, {
                    Log.i("FUCKERROR :: ", it.toString() + " NO")
                })
        )
        Log.i("FUCKERROR :: ", countryList.toString())

        saveEpisodes(seasonList)
    }

    private fun saveEpisodes(seasonList: List<SeasonModel>) {
        val episodeList: MutableList<Episode> = ArrayList()
        seasonList.forEach { season ->
            season.episodeList.forEach { episode ->
                episodeList.add(MovieMapper.episodeModelToEpisode(episode, season, netflixId))
            }
        }

        compositeDisposable.add(
            localRepository.saveEpisodes(episodeList)
                .subscribeOn(async.io())
                .observeOn(async.io())
                .subscribe({
                    Log.i("SAVEEPISODES :: ", it.toString())
                }, {
                    Log.i("SAVEEPISODES :: ", it.message.toString() + " ERROR")
                })
        )
    }

    fun deleteMovie() {
        compositeDisposable.add(
            localRepository.deleteMovie(movie)
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
//                    isSavedSubject.onNext(false)
                    movieDetailSubject.onNext(ShowIsSavedMovie(false))
                }, {
                    Log.i("DELETERROR :: ", it.message.toString())
                })
        )
    }

    private fun checkMovie(netflixId: Int) {
        compositeDisposable.add(
            localRepository.checkMovie(netflixId)
                .subscribeOn(async.io())
                .subscribe({
                    Log.i("CHEKMOVIEERR :: ", it.toString() + " FUCK")
                    if (it != null) {
                        if (it > 0) {
                            getMovieFromDatabase(netflixId)
//                            checkSavedSubject.onNext(true)
                            movieDetailSubject.onNext(CheckSavedMovie(true))
                        } else {
//                            checkSavedSubject.onNext(false)
                            movieDetailSubject.onNext(CheckSavedMovie(false))
                            getMovieFromNetwork(netflixId)
                            getMovieCountries(netflixId)
                        }
                    } else {
                        getMovieFromNetwork(netflixId)
                        getMovieCountries(netflixId)
//                        checkSavedSubject.onNext(false)
                        movieDetailSubject.onNext(CheckSavedMovie(false))
                    }
                }, {
                    getMovieFromNetwork(netflixId)
                    getMovieCountries(netflixId)
//                    checkSavedSubject.onNext(false)
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
//                    seasonSubject.onNext(it)

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
//                    episodeSubject.onNext(it)
                    movieDetailSubject.onNext(ShowEpisodes(it))
                }, {
                    Log.i("SPERROR :: ", it.message.toString())
                })
        )
    }

}