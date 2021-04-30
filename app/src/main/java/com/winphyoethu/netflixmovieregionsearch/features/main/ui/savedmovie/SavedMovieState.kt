package com.winphyoethu.netflixmovieregionsearch.features.main.ui.savedmovie

import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote

sealed class SavedMovieState
object Loading : SavedMovieState()
data class Empty(var message: String): SavedMovieState()
data class Show(var movieList: List<MovieRemote>): SavedMovieState()
data class Error(var message: String): SavedMovieState()