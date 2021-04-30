package com.winphyoethu.netflixmovieregionsearch.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Country
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCountry(countryList: List<Country>): Maybe<List<Long>>

    @Query("SELECT * FROM country")
    fun getCountry(): Single<List<Country>>

}