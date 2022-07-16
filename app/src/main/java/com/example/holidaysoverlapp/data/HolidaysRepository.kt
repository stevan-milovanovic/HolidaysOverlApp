package com.example.holidaysoverlapp.data

import com.example.holidaysoverlapp.data.Constants.YEAR
import com.example.holidaysoverlapp.data.api.HolidaysService
import com.example.holidaysoverlapp.data.db.CountryDao
import com.example.holidaysoverlapp.data.db.HolidayDao
import com.example.holidaysoverlapp.data.entities.Country
import com.example.holidaysoverlapp.data.entities.Holiday
import javax.inject.Inject

class HolidaysRepository @Inject constructor(
    private val holidaysService: HolidaysService,
    private val countryDao: CountryDao,
    private val holidayDao: HolidayDao
) {

    val countriesFlow = countryDao.getAllAsFlow()
    val holidaysFlow = holidayDao.getAllAsFlow()

    suspend fun toggleCountrySelectedState(countryCode: String) {
        val selectedCountries = countryDao.getSelected()
        val countryCodeIsSelected = selectedCountries.map { it.code }.contains(countryCode)
        if (selectedCountries.size >= 2 && !countryCodeIsSelected) {
            for (i in selectedCountries.size - 2 downTo 0) {
                toggleCountrySelectedState(selectedCountries[i])
            }
        }

        countryDao.getForCode(countryCode)?.let {
            toggleCountrySelectedState(it)
        }
    }

    private suspend fun toggleCountrySelectedState(country: Country) {
        val updatedCountry = country.copy(
            selected = !country.selected
        )
        countryDao.updateSelectedState(updatedCountry)
    }

    suspend fun loadCountries() {
        val localCountries = countryDao.getAll()
        val remoteCountriesDTO = holidaysService.getCountries()

        if (remoteCountriesDTO.countries.isEmpty()) {
            countryDao.deleteAll()
            return
        }

        val remoteCountries = remoteCountriesDTO.countries.map { countryDTO ->
            with(countryDTO) { Country(code, name, flag, false) }
        }

        val orphanCountries = localCountries.filter { !remoteCountries.contains(it) }
        countryDao.delete(orphanCountries)
        countryDao.insert(remoteCountries)
    }

    suspend fun loadHolidays(countryCodeOne: String, countryCodeTwo: String) {
        val holidays = holidayDao.getAll()
        val orphans =
            holidays.filter { it.country != countryCodeOne && it.country != countryCodeTwo }
        holidayDao.delete(orphans)

        loadHolidays(countryCodeOne)
        loadHolidays(countryCodeTwo)
    }

    private suspend fun loadHolidays(countryCode: String) {
        val localHolidays = holidayDao.getAllForCountry(countryCode)
        val remoteHolidaysDTO = holidaysService.getHolidays(countryCode, YEAR)

        if (remoteHolidaysDTO.holidays.isEmpty()) {
            holidayDao.deleteAllForCountry(countryCode)
            return
        }

        val remoteHolidays = remoteHolidaysDTO.holidays.map { holidayDTO ->
            with(holidayDTO) { Holiday(uuid, name, date, country) }
        }

        val orphanHolidays = localHolidays.filter { !remoteHolidays.contains(it) }
        holidayDao.delete(orphanHolidays)
        holidayDao.insert(remoteHolidays)
    }

    suspend fun clearHolidays(holidays: List<Holiday>) {
        holidayDao.delete(holidays)
    }

}