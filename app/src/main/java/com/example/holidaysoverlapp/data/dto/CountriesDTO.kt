package com.example.holidaysoverlapp.data.dto

data class CountriesDTO(
    val countries: List<CountryDTO>
) {

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?) = (other as? CountriesDTO)?.let {
        it.countries == countries
    } ?: false

}