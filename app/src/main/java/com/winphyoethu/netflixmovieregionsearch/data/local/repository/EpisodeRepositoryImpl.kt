package com.winphyoethu.netflixmovieregionsearch.data.local.repository

import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.EpisodeDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import com.winphyoethu.netflixmovieregionsearch.domain.repository.EpisodeRepository
import io.reactivex.Maybe
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(val episodeDao: EpisodeDao): EpisodeRepository {

    override fun saveEpisodes(episodeList: List<Episode>): Maybe<List<Long>> {
        return episodeDao.saveEpisodes(episodeList)
    }

}