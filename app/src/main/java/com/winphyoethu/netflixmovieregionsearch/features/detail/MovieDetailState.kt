package com.winphyoethu.netflixmovieregionsearch.features.detail

import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.CountryDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.MovieDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.EpisodeModel
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.SeasonModel

sealed class MovieDetailState
data class ShowMovieDetail(val movieDetail: MovieDetail) : MovieDetailState()
data class ShowCountries(val countryDetail: List<CountryDetail>) : MovieDetailState()
data class ShowSeasons(val season: List<SeasonModel>) : MovieDetailState()
data class ShowEpisodes(val episodes: List<EpisodeModel>) : MovieDetailState()
data class ShowIsSavedMovie(val boolean: Boolean) : MovieDetailState()
data class CheckSavedMovie(val boolean: Boolean) : MovieDetailState()
data class ShowError(val message: String, val isSavedError: Boolean): MovieDetailState()