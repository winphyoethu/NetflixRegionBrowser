package com.winphyoethu.netflixmovieregionsearch.features.main.ui.home

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.winphyoethu.netflixmovieregionsearch.data.remote.ApiService
import com.winphyoethu.netflixmovieregionsearch.data.remote.RemoteRepository
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.AllMovie
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.features.main.FilterModel
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class HomeDataSource(
    val remoteRepository: RemoteRepository,
    var filter: FilterModel,
    val async: Async,
    val initialNetworkState: PublishSubject<NetworkState>,
    val networkState: PublishSubject<NetworkState>
) :
    ItemKeyedDataSource<Int, MovieRemote>() {

    private var newDate: String = ""
    private var genre: String = ""
    private var orderBy: String = ""
    private var audioSubtitleAndOr: String = ""
    private var startRating: String = ""
    private var limit: String = "48"
    private var subtitle: String = ""
    private var audio: String = ""
    private var countryAndOrUnique: String = ""
    private var offset: String = "0"
    private var page = 0

    private val compositeDisposable = CompositeDisposable()

    private var params: LoadParams<Int>? = null
    private var callback: LoadCallback<MovieRemote>? = null

    override fun getKey(item: MovieRemote): Int = item.id!!

    override fun loadInitial(
        params: LoadInitialParams<Int>, callback: LoadInitialCallback<MovieRemote>
    ) {
        initialNetworkState.onNext(NetworkLoading)
        compositeDisposable.add(
            remoteRepository.getMovieList(
                newDate, genre, filter.movieType, filter.startYear, orderBy, audioSubtitleAndOr,
                filter.startRating, limit, filter.endRating, subtitle, filter.selectedCountriesList,
                filter.query, audio, countryAndOrUnique, offset, filter.endYear
            )
                .subscribe({
                    page++
                    it?.let {
                        if (it.movieList.isNullOrEmpty()) {
                            initialNetworkState.onNext(InitialEmpty)
                        } else {
                            callback.onResult(it.movieList)
                        }
                    }
                }, {
                    if (it is HttpException) {
                        when (it.code()) {
                            404 -> initialNetworkState.onNext(NetworkError("Not Found"))
                            500 -> initialNetworkState.onNext(NetworkError("Server Error"))
                            else -> initialNetworkState.onNext(NetworkError("No Internet Connection"))
                        }
                    } else {
                        initialNetworkState.onNext(NetworkError("No Internet Connection"))
                    }
                })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<MovieRemote>) {
        networkState.onNext(NetworkLoading)
        compositeDisposable.add(
            remoteRepository.getMovieList(
                newDate, genre, filter.movieType, filter.startYear, orderBy, audioSubtitleAndOr,
                startRating, limit, filter.endRating, subtitle, filter.selectedCountriesList,
                filter.query, audio, countryAndOrUnique, (page * limit.toInt() + 1).toString(),
                filter.endYear
            )
                .subscribeOn(async.io())
                .subscribe({
                    this.params = null
                    this.callback = null

                    page++
                    it?.let {
                        if (it.movieList.isNullOrEmpty()) {
                            networkState.onNext(NetworkEmpty)
                        } else {
                            callback.onResult(it.movieList)
                        }
                    }
                }, {
                    this.params = params
                    this.callback = callback

                    if (it is HttpException) {
                        when (it.code()) {
                            404 -> networkState.onNext(NetworkError("Not Found"))
                            500 -> networkState.onNext(NetworkError("Server Error"))
                            else -> networkState.onNext(NetworkError("No Internet Connection"))
                        }
                    } else if (it is IOException) {
                        networkState.onNext(NetworkError("No Internet Connection"))
                    }
                })
        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<MovieRemote>) {
        Log.i("FUCKSHITDICK :: ", "IT IS HERER")
    }

    fun retryNext() {
        if (params != null && callback != null) {
            loadAfter(params!!, callback!!)
        }
    }

}