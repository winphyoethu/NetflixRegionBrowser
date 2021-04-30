package com.winphyoethu.netflixmovieregionsearch.util.mapper

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Movie
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.movie.MovieRemote
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.CountryDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.MovieDetail
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.EpisodeModel
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.SeasonModel

object MovieMapper {

    fun movieToMovieRemote(movie: Movie): MovieRemote {
        val movieRemote = MovieRemote(
            id = null,
            title = movie.title,
            img = movie.img,
            videoType = movie.videoType,
            netflixId = movie.netflixId,
            synopsis = movie.synopsis,
            imdbRating = movie.rating,
            year = movie.year,
            poster = movie.poster,
            titleDate = null,
            cList = null
        )

        return movieRemote
    }

    fun movieRemoteToMovie(movieRemote: MovieRemote): Movie {
        val movie = Movie(
            title = movieRemote.title,
            rating = movieRemote.imdbRating.toString(),
            videoType = movieRemote.videoType,
            synopsis = movieRemote.synopsis,
            plot = null,
            poster = movieRemote.poster,
            img = movieRemote.img,
            netflixId = movieRemote.netflixId,
            runtime = null,
            genre = null,
            year = movieRemote.year,
            maturityLabel = null
        )

        return movie
    }

    fun movieDetailToMovie(movieDetail: MovieDetail): Movie {
        val movie = Movie(
            title = movieDetail.title,
            rating = movieDetail.imdbRating,
            videoType = movieDetail.vType,
            synopsis = movieDetail.synopsis,
            plot = movieDetail.imdbPlot,
            poster = movieDetail.imdbPoster,
            img = movieDetail.image,
            netflixId = movieDetail.netflixId,
            runtime = movieDetail.imdbRuntime,
            genre = movieDetail.imdbGenre,
            year = movieDetail.year,
            maturityLabel = movieDetail.matLabel
        )

        return movie
    }

    fun movieToMovieDetail(movie: Movie): MovieDetail {
        return MovieDetail(
            title = movie.title,
            imdbRating = movie.rating,
            vType = movie.videoType,
            synopsis = movie.synopsis,
            imdbPlot = movie.plot,
            imdbPoster = movie.poster,
            image = movie.img,
            netflixId = movie.netflixId,
            imdbRuntime = movie.runtime,
            imdbGenre = movie.genre,
            year = movie.year,
            avgRating = null,
            nfDate = null,
            matLabel = movie.maturityLabel
        )
    }

    fun countryDetailToAvailableCountry(
        countryDetail: CountryDetail, netflixId: Int
    ): AvailableCountry {
        return AvailableCountry(
            netflixId = netflixId,
            subtitle = countryDetail.subtitle,
            countryCode = countryDetail.cc,
            country = countryDetail.country,
            audio = countryDetail.audio
        )
    }

    fun availableCountryToCountryDetail(
        availableCountryList: List<AvailableCountry>
    ): List<CountryDetail> {
        val countryDetailList: MutableList<CountryDetail> = ArrayList()

        availableCountryList.forEach {
            val countryDetail = CountryDetail(
                subtitle = it.subtitle,
                cc = it.countryCode,
                country = it.country,
                audio = it.audio,
                newDate = null,
                uhd = null,
                expireDate = null
            )
            countryDetailList.add(countryDetail)
        }

        return countryDetailList
    }

    fun episodeToSeasonModel(episodeList: List<Episode>): List<SeasonModel> {
        val seasonList: MutableList<SeasonModel> = ArrayList()

        val seasonMap: HashMap<Int, MutableList<EpisodeModel>> = HashMap()

        episodeList.forEach {
            if (seasonMap.isEmpty()) {
                seasonMap[it.seasonId] = mutableListOf(
                    EpisodeModel(
                        netflixId = it.netflixId!!,
                        episodeId = it.episodeId,
                        seasonId = it.seasonId,
                        episodeNumber = null,
                        seasonNumber = it.seasonId,
                        synopsis = it.synopsis,
                        title = it.episodeTitle,
                        image = null
                    )
                )
            } else {
                var episodeModelList = seasonMap[it.seasonId]

                if (episodeModelList.isNullOrEmpty()) {
                    episodeModelList = mutableListOf(
                        EpisodeModel(
                            netflixId = it.netflixId!!,
                            episodeId = it.episodeId,
                            seasonId = it.seasonId,
                            episodeNumber = null,
                            seasonNumber = it.seasonId,
                            synopsis = it.synopsis,
                            title = it.episodeTitle,
                            image = null
                        )
                    )
                } else {
                    episodeModelList.add(
                        EpisodeModel(
                            netflixId = it.netflixId!!,
                            episodeId = it.episodeId,
                            seasonId = it.seasonId,
                            episodeNumber = null,
                            seasonNumber = it.seasonId,
                            synopsis = it.synopsis,
                            title = it.episodeTitle,
                            image = null
                        )
                    )
                }

                seasonMap[it.seasonId] = episodeModelList
            }
        }

        for (season in seasonMap) {
            seasonList.add(SeasonModel(season = season.key, episodeList = season.value))
        }
        seasonList.sortBy {
            it.season
        }

        return seasonList
    }

    fun episodeToSeasonModel1(episodeList: List<Episode>): List<SeasonModel> {
        val seasonList: MutableList<SeasonModel> = ArrayList()
        val seasonIdList: MutableList<Int> = ArrayList()

        episodeList.forEach {
            if (seasonList.isEmpty()) {
                seasonIdList.add(it.seasonId)
                seasonList.add(
                    SeasonModel(
                        it.seasonId, mutableListOf(
                            EpisodeModel(
                                netflixId = it.netflixId!!,
                                episodeId = it.episodeId,
                                seasonId = it.seasonId,
                                episodeNumber = null,
                                seasonNumber = it.seasonId,
                                synopsis = it.synopsis,
                                title = it.episodeTitle,
                                image = null
                            )
                        )
                    )
                )
            } else {
                if (seasonIdList.contains(it.seasonId)) {
                    seasonList[it.seasonId].episodeList.add(
                        EpisodeModel(
                            netflixId = it.netflixId!!,
                            episodeId = it.episodeId,
                            seasonId = it.seasonId,
                            episodeNumber = null,
                            seasonNumber = it.seasonId,
                            synopsis = it.synopsis,
                            title = it.episodeTitle,
                            image = null
                        )
                    )
                } else {
                    seasonList.add(
                        SeasonModel(
                            it.seasonId, mutableListOf(
                                EpisodeModel(
                                    netflixId = it.netflixId!!,
                                    episodeId = it.episodeId,
                                    seasonId = it.seasonId,
                                    episodeNumber = null,
                                    seasonNumber = it.seasonId,
                                    synopsis = it.synopsis,
                                    title = it.episodeTitle,
                                    image = null
                                )
                            )
                        )
                    )
                }
            }
        }

        return seasonList
    }

    fun episodeModelToEpisode(episode: EpisodeModel, season: SeasonModel, netflixId: Int): Episode {
        return Episode(
            episodeId = episode.episodeId,
            seasonId = season.season,
            episodeTitle = episode.title,
            synopsis = episode.synopsis,
            netflixId = netflixId
        )
    }

}