package com.winphyoethu.netflixmovieregionsearch.data.local.repository

import com.winphyoethu.netflixmovieregionsearch.data.local.database.dao.CountryDao
import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Country
import com.winphyoethu.netflixmovieregionsearch.domain.repository.CountryRepository
import io.reactivex.Maybe
import io.reactivex.Single
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(val countryDao: CountryDao): CountryRepository {

    override fun getCountries(): Single<List<Country>> {
        return countryDao.getCountry()
    }

    override fun saveCountries(countryList: List<Country>): Maybe<List<Long>> {
        return countryDao.insertCountry(countryList)
    }

}