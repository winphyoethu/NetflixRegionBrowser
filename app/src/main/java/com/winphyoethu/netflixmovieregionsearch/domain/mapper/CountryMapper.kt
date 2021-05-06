package com.winphyoethu.netflixmovieregionsearch.domain.mapper

import com.winphyoethu.netflixmovieregionsearch.data.local.database.entities.Country
import com.winphyoethu.netflixmovieregionsearch.data.remote.model.country.CountryRemote

object CountryMapper {

    fun countryRemoteToCountry(countryRemoteList: List<CountryRemote>): List<Country> {

        val countryList: MutableList<Country> = ArrayList()

        for (country in countryRemoteList) {
            countryList.add(
                Country(countryCode = country.countryCode, country = country.country, countryId = country.id)
            )
        }

        return countryList

    }

}