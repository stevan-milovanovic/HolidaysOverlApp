package com.example.holidaysoverlapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.holidaysoverlapp.data.HolidaysRepository
import com.example.holidaysoverlapp.data.entities.Country
import com.example.holidaysoverlapp.data.entities.Holiday
import com.example.holidaysoverlapp.ui.util.EntityListViewState
import com.example.holidaysoverlapp.ui.util.EntityListViewState.*
import com.example.holidaysoverlapp.ui.util.ErrorType
import com.example.holidaysoverlapp.ui.util.HolidaysFilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: HolidaysRepository
) : ViewModel() {

    private val _countryViewState =
        MutableStateFlow<EntityListViewState<Country>>(Success(emptyList()))
    val countryViewState: StateFlow<EntityListViewState<Country>> = _countryViewState

    private val _holidayViewState =
        MutableStateFlow<EntityListViewState<Holiday>>(Success(emptyList()))
    val holidayViewState: StateFlow<EntityListViewState<Holiday>> = _holidayViewState

    private var filterType: HolidaysFilterType = HolidaysFilterType.Intersection
    private var countryA: Country? = null
    private var countryB: Country? = null
    private var holidays: List<Holiday> = emptyList()

    init {
        viewModelScope.launch {
            repository.countriesFlow
                .catch {
                    _countryViewState.value = Error(ErrorType.Unknown)
                }
                .collect { countries ->
                    _countryViewState.value = Success(countries)
                    val selected = countries.filter { it.selected }
                    if (selected.size == 2) {
                        countryA = selected.first()
                        countryB = selected.last()
                        loadHolidays(countryA!!, countryB!!)
                    } else {
                        clearHolidays(holidays)
                    }
                }
        }

        viewModelScope.launch {
            repository.holidaysFlow
                .catch {
                    _holidayViewState.value = Error(ErrorType.Unknown)
                }
                .collect { holidays ->
                    val firstCountry = countryA ?: return@collect
                    val secondCountry = countryB ?: return@collect
                    val first = holidays.filter { it.country == firstCountry.code }.toSet()
                    val second = holidays.filter { it.country == secondCountry.code }.toSet()
                    this@HomeViewModel.holidays = when (filterType) {
                        HolidaysFilterType.OnlyA -> (first - second).toList()
                        HolidaysFilterType.OnlyB -> (second - first).toList()
                        HolidaysFilterType.Intersection -> (first.intersect(second)).toList()
                    }
                    _holidayViewState.value = Success(this@HomeViewModel.holidays)
                }
        }

        loadCountries()
    }

    fun refresh() {
        if (countryA != null && countryB != null) {
            loadHolidays(countryA!!, countryB!!)
        }
    }

    fun changeFilterType(filterType: HolidaysFilterType) {
        this.filterType = filterType
        refresh()
    }

    fun toggleCountrySelectedState(countryCode: String) {
        viewModelScope.launch {
            repository.toggleCountrySelectedState(countryCode)
        }
    }

    private fun loadHolidays(countryOne: Country, countryTwo: Country) {
        _holidayViewState.value = Loading
        viewModelScope.launch {
            try {
                repository.loadHolidays(countryOne.code, countryTwo.code)
            } catch (e: UnknownHostException) {
                _holidayViewState.value = Error(ErrorType.NoInternet)
            }
        }
    }

    private fun clearHolidays(holidays: List<Holiday>) {
        viewModelScope.launch {
            try {
                repository.clearHolidays(holidays)
            } catch (e: UnknownHostException) {
                _holidayViewState.value = Error(ErrorType.NoInternet)
            }
        }
    }

    private fun loadCountries() {
        _countryViewState.value = Loading
        viewModelScope.launch {
            try {
                repository.loadCountries()
            } catch (e: UnknownHostException) {
                _countryViewState.value = Error(ErrorType.NoInternet)
            }
        }
    }

}