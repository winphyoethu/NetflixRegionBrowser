package com.winphyoethu.netflixmovieregionsearch.features.main.ui.savedmovie

import android.util.Log
import androidx.lifecycle.ViewModel
import com.winphyoethu.netflixmovieregionsearch.data.local.LocalRepository
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.util.mapper.MovieMapper
import com.winphyoethu.netflixmovieregionsearch.util.rx.Async
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SavedMovieViewModel @Inject constructor(
    val localRepository: LocalRepository, val async: Async
) : ViewModel() {

    val savedMovieSubject: PublishSubject<SavedMovieState> = PublishSubject.create()

    private val compositeDisposable = CompositeDisposable()

    fun getMovie() {
        compositeDisposable.add(
            localRepository.getMovieList()
                .map {
                    val movieRemoteList: MutableList<MovieRemote> = ArrayList()

                    it.forEach { movie ->
                        movieRemoteList.add(MovieMapper.movieToMovieRemote(movie))
                    }

                    movieRemoteList
                }
                .delay(150, TimeUnit.MILLISECONDS)
                .subscribeOn(async.io())
                .observeOn(async.main())
                .subscribe({
                    if (it.isNullOrEmpty()) {
                        savedMovieSubject.onNext(Empty("Empty List"))
                    } else {
                        savedMovieSubject.onNext(Show(it))
                    }
                }, {
                    savedMovieSubject.onNext(Error("Error Loading"))
                })
        )
    }

}