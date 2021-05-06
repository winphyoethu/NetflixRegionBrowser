package com.winphyoethu.netflixmovieregionsearch.domain.repository

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Country
import io.reactivex.Maybe
import io.reactivex.Single

interface CountryRepository {

    fun getCountries(): Single<List<Country>>

    fun saveCountries(countryList: List<Country>): Maybe<List<Long>>

}