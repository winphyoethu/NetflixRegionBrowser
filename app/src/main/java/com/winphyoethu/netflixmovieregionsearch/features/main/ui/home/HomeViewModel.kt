package com.winphyoethu.netflixmovieregionsearch.features.main.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.winphyoethu.netflixmovieregionsearch.data.local.LocalRepository
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Country
import com.winphyoethu.netflixmovieregionsearch.data.remote.RemoteRepository
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.features.main.FilterModel
import com.winphyoethu.netflixmovieregionsearch.util.mapper.CountryMapper
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    val remoteRepository: RemoteRepository, val asyncManager: Async, val localRepository: LocalRepository
) : ViewModel() {

    val countrySubject: PublishSubject<List<Country>> = PublishSubject.create()
    val homeViewStateSubject: PublishSubject<HomeViewState> = PublishSubject.create()
    val filterSubject: PublishSubject<FilterModel> = PublishSubject.create()

    private val initialNetworkSubject: PublishSubject<NetworkState> = PublishSubject.create()
    private val networkSubject: PublishSubject<NetworkState> = PublishSubject.create()

    var movieRemoteList: Observable<PagedList<MovieRemote>>

    private val countryList: MutableList<Country> = ArrayList()

    // Filter Fields
    private val filter = FilterModel()

    var homeSourceFactory: HomeDataFactory
    private val compositeDisposable = CompositeDisposable()

    private var homeState: HomeViewState? = null

    init {
        Log.i("localrepo :: ", localRepository.hashCode().toString())
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .build()

        homeSourceFactory = HomeDataFactory(
            remoteRepository, filter, asyncManager, initialNetworkSubject, networkSubject
        )

        movieRemoteList = RxPagedListBuilder(homeSourceFactory, config)
            .setFetchScheduler(asyncManager.io())
            .setNotifyScheduler(asyncManager.main())
            .buildObservable()
            .cache()
    }

    fun retryNext() {
        homeSourceFactory.homeDataSource!!.retryNext()
    }

    fun setQuery(query: String) {
        filter.query = query
        homeViewStateSubject.onNext(Filter(filter))
    }

    fun setYear(startYear: String) {
        filter.startYear = startYear
        homeViewStateSubject.onNext(Filter(filter))
    }

    fun setImdbRating(rating: String) {
        filter.startRating = rating
        homeViewStateSubject.onNext(Filter(filter))
    }

    fun setMovieOrSeries(type: String) {
        filter.movieType = type
        homeViewStateSubject.onNext(Filter(filter))
    }

    fun setAllCountrySelected(isChecked: Boolean) {
        filter.selectedCountries = 0
        filter.selectedCountriesList = ""
        if (isChecked) {
            for (country in countryList) {
                if (filter.selectedCountriesList.isEmpty()) {
                    filter.selectedCountriesList += country.countryId
                } else {
                    filter.selectedCountriesList += "," + country.countryId
                }
            }
            filter.selectedCountries = countryList.size
        }

        homeViewStateSubject.onNext(Filter(filter))
    }

    fun setSpecificCountrySelected(position: Int, isChecked: Boolean) {
        if (isChecked) {
            if (filter.selectedCountriesList.isEmpty()) {
                filter.selectedCountriesList += countryList[position].countryId
            } else {
                filter.selectedCountriesList += "," + countryList[position].countryId
            }
            filter.selectedCountries = filter.selectedCountriesList.split(",").size
            homeViewStateSubject.onNext(Filter(filter))
        } else {
            if (filter.selectedCountriesList.isNotEmpty()) {
                val tempList = filter.selectedCountriesList.split(",").toMutableList()
                if (tempList.contains(countryList[position].countryId.toString())) {
                    tempList.remove(countryList[position].countryId.toString())
                }
                val tempAry = tempList.toTypedArray()

                filter.selectedCountriesList = if (tempAry.isNotEmpty()) {
                    tempAry.joinToString(separator = ",")
                } else {
                    ""
                }
                filter.selectedCountries = tempAry.size
                homeViewStateSubject.onNext(Filter(filter))
            } else {
                filter.selectedCountriesList = ""
                filter.selectedCountries = 0
                homeViewStateSubject.onNext(Filter(filter))
            }
        }
    }

    private fun getCountries() {
        compositeDisposable.add(
            remoteRepository.getCountries()
                .subscribeOn(asyncManager.io())
                .observeOn(asyncManager.io())
                .map {
                    CountryMapper.countryRemoteToCountry(it.countryList)
                }
                .subscribe({
                    countryList.addAll(it)
                    saveCountries(it)
                }, {
                    Log.i("SAVEERROR1 :: ", it.message + " :: FUCK")
                })
        )
    }

    fun invalidate() {
        homeViewStateSubject.onNext(Loading)
        homeViewStateSubject.onNext(EmptyList)
        homeSourceFactory.homeDataSource!!.invalidate()
        homeState = null
    }

    fun checkCountries() {
        if (countryList.isEmpty()) {
            compositeDisposable.add(
                localRepository.getCountries()
                    .subscribeOn(asyncManager.io())
                    .observeOn(asyncManager.main())
                    .subscribe({
                        if (it.isEmpty()) {
                            getCountries()
                        } else {
                            countryList.addAll(it)
                            countrySubject.onNext(it)
                        }
                    }, {
                        getCountries()
                    })
            )
        } else {
            compositeDisposable.add(
                Single.just(countryList)
                    .observeOn(asyncManager.main())
                    .subscribe({
                        countrySubject.onNext(it)
                    }, {

                    })
            )
        }
    }

    private fun saveCountries(countryList: List<Country>) {
        compositeDisposable.add(
            localRepository.saveCountries(countryList)
                .subscribeOn(asyncManager.io())
                .subscribe({
                    Log.i("SAVEERROR NOT :: ", it.toString() + " :: FUCK")
                }, {
                    Log.i("SAVEERROR :: ", it.message + " :: FUCK")
                })
        )
    }

    fun observeMovie() {
//        homeViewStateSubject.onNext(Loading)
        if (homeState != null && (homeState is Empty || homeState is Error)) {
            if (homeSourceFactory.homeDataSource != null) {
                Log.i("doggymama :: ", "yes it is")
                homeSourceFactory.homeDataSource!!.invalidate()
                homeState = null
            }
        }

        compositeDisposable.add(
            Single.just(filter)
                .subscribeOn(asyncManager.io())
                .observeOn(asyncManager.main())
                .subscribe({
                    homeViewStateSubject.onNext(Filter(it))
                }, {

                })
        )

        compositeDisposable.add(
            movieRemoteList
                .delay(170, TimeUnit.MILLISECONDS)
                .filter {
                    it.isNotEmpty()
                }
                .subscribe({
                    Log.i("doggyshit :: ", it.toString())
                    homeViewStateSubject.onNext(Show(it))
                }, {
                    Log.i("doggy :: ", it.message.toString() + " DICK")
                })
        )

        compositeDisposable.add(
            initialNetworkSubject
                .delay(270, TimeUnit.MILLISECONDS)
                .observeOn(asyncManager.main())
                .subscribe({
                    when (it) {
                        is NetworkLoading -> homeViewStateSubject.onNext(Loading)
                        is InitialEmpty -> {
                            homeViewStateSubject.onNext(Empty("No Movie found"))
                            homeState = Empty("No Movie Found")
                        }
                        else -> {
                            it as NetworkError
                            homeViewStateSubject.onNext(Error(it.message!!))
                            homeState = Error("No Movie Found")
                        }
                    }
                }, {
                    Log.i("fuckshit :: ", "satpat" + it.localizedMessage.toString())
                })
        )

        compositeDisposable.add(
            networkSubject
                .observeOn(asyncManager.main())
                .subscribe({
                    when (it) {
                        is NetworkLoading -> homeViewStateSubject.onNext(NextLoading)
                        is NetworkEmpty -> homeViewStateSubject.onNext(NextEmpty("All movie has been showed"))
                        else -> {
                            it as NetworkError
                            homeViewStateSubject.onNext(NextError(it.message!!))
                        }
                    }
                }, {

                })
        )
    }

    fun observeFilter() {
        compositeDisposable.add(
            Single.just(filter)
                .subscribeOn(asyncManager.io())
                .observeOn(asyncManager.main())
                .subscribe({
                    Log.i("OBSERVE", it.toString())
                    filterSubject.onNext(it)

                }, {

                })
        )
    }

}