package com.winphyoethu.netflixmovieregionsearch.features.main.ui.home

import androidx.paging.DataSource
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.domain.repository.RemoteRepository
import com.winphyoethu.netflixmovieregionsearch.features.main.FilterModel
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import io.reactivex.subjects.PublishSubject

class HomeDataFactory constructor(
    var remoteRepository: RemoteRepository, var filter: FilterModel, val async: Async,
    val initialNetworkSubject: PublishSubject<NetworkState>,
    val networkSubject: PublishSubject<NetworkState>
) : DataSource.Factory<Int, MovieRemote>() {

    var homeDataSource: HomeDataSource? =null

    override fun create(): DataSource<Int, MovieRemote> {
        homeDataSource = HomeDataSource(remoteRepository, filter, async, initialNetworkSubject, networkSubject)
        return homeDataSource!!
    }

}