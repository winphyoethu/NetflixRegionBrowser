package com.winphyoethu.netflixmovieregionsearch.domain.repository

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import io.reactivex.Maybe

interface EpisodeRepository {

    fun saveEpisodes(episodeList: List<Episode>): Maybe<List<Long>>

}