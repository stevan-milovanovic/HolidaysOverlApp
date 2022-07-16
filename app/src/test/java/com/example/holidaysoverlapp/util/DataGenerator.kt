package com.example.holidaysoverlapp.util

import com.example.holidaysoverlapp.data.dto.CountryDTO
import com.example.holidaysoverlapp.data.entities.Country
import com.example.holidaysoverlapp.data.entities.Holiday

object DataGenerator {

    fun getCountriesDtos() = listOf(
        CountryDTO(
            "AD",
            "Andorra",
            "https://www.countryflags.io/AD/flat/64.png"
        ),
        CountryDTO(
            "AE",
            "United Arab Emirates",
            "https://www.countryflags.io/AE/flat/64.png"
        ),
        CountryDTO(
            "AF",
            "Afghanistan",
            "https://www.countryflags.io/AF/flat/64.png"
        )
    )

    fun getCountries() = listOf(
        Country(
            "AD",
            "Andorra",
            "https://www.countryflags.io/AD/flat/64.png",
            false
        ),
        Country(
            "AE",
            "United Arab Emirates",
            "https://www.countryflags.io/AE/flat/64.png",
            false
        ),
        Country(
            "AF",
            "Afghanistan",
            "https://www.countryflags.io/AF/flat/64.png",
            false
        )
    )

    fun getHolidays() = listOf(
        Holiday(
            "840dc442-e301-43b3-820f-bf56138eb985",
            "New Year's Day",
            "2021-01-01",
            "RS"
        ),
        Holiday(
            "556f4bbd-a487-468e-a00c-c8370e19d4e8",
            "Second Day of the New Year",
            "2021-01-02",
            "RS"
        )
    )

}