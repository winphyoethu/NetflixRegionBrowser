package com.winphyoethu.netflixmovieregionsearch.domain.mapper

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.EpisodeModel
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.moviedetail.season.SeasonModel

object SeasonEpisodeMapper {

    fun episodeToSeasonModel(episodeList: List<Episode>): List<SeasonModel> {
        val seasonList: MutableList<SeasonModel> = ArrayList()

        val seasonMap: HashMap<Int, MutableList<EpisodeModel>> = HashMap()

        episodeList.forEach {
            if (seasonMap.isEmpty()) {
                seasonMap[it.seasonId] = mutableListOf(
                    EpisodeModel(
                        netflixId = it.netflixId!!, episodeId = it.episodeId, seasonId = it.seasonId,
                        episodeNumber = null, seasonNumber = it.seasonId, synopsis = it.synopsis,
                        title = it.episodeTitle, image = null
                    )
                )
            } else {
                var episodeModelList = seasonMap[it.seasonId]

                if (episodeModelList.isNullOrEmpty()) {
                    episodeModelList = mutableListOf(
                        EpisodeModel(
                            netflixId = it.netflixId!!, episodeId = it.episodeId, seasonId = it.seasonId,
                            episodeNumber = null, seasonNumber = it.seasonId, synopsis = it.synopsis,
                            title = it.episodeTitle, image = null
                        )
                    )
                } else {
                    episodeModelList.add(
                        EpisodeModel(
                            netflixId = it.netflixId!!, episodeId = it.episodeId, seasonId = it.seasonId,
                            episodeNumber = null, seasonNumber = it.seasonId, synopsis = it.synopsis,
                            title = it.episodeTitle, image = null
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

    fun episodeModelToEpisode(episode: EpisodeModel, season: SeasonModel, netflixId: Int): Episode {
        return Episode(
            episodeId = episode.episodeId, seasonId = season.season, episodeTitle = episode.title,
            synopsis = episode.synopsis, netflixId = netflixId
        )
    }

}