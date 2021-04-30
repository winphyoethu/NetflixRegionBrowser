package com.winphyoethu.netflixmovieregionsearch.features.main.ui.home

import androidx.paging.PagedList
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.features.main.FilterModel

sealed class HomeViewState
object Loading : HomeViewState()
data class Error(var message: String) : HomeViewState()
data class Show(var movieList: PagedList<MovieRemote>) : HomeViewState()
object EmptyList: HomeViewState()
data class Empty(var message: String) : HomeViewState()
data class Filter(var filter: FilterModel) : HomeViewState()
object NextLoading : HomeViewState()
data class NextError(var message: String) : HomeViewState()
data class NextEmpty(var message: String) : HomeViewState()