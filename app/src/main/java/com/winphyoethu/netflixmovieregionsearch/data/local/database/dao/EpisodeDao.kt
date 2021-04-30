package com.winphyoethu.netflixmovieregionsearch.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Episode
import io.reactivex.Maybe

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveEpisodes(episodeList: List<Episode>): Maybe<List<Long>>

}