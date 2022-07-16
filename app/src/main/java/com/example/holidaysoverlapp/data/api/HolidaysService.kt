package com.example.holidaysoverlapp.data.api

import com.example.holidaysoverlapp.data.dto.CountriesDTO
import com.example.holidaysoverlapp.data.dto.HolidaysDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface HolidaysService {

    @GET("countries")
    suspend fun getCountries(): CountriesDTO

    @GET("holidays")
    suspend fun getHolidays(
        @Query("country") country: String,
        @Query("year") year: String
    ): HolidaysDTO

}