package com.winphyoethu.netflixmovieregionsearch.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.AvailableCountry
import io.reactivex.Maybe

@Dao
interface AvailableCountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveAvailableCountry(availableCountryList: List<AvailableCountry>): Maybe<List<Long>>

}