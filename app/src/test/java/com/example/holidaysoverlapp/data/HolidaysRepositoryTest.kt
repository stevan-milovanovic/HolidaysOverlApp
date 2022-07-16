package com.example.holidaysoverlapp.data

import com.example.holidaysoverlapp.data.api.HolidaysService
import com.example.holidaysoverlapp.data.db.CountryDao
import com.example.holidaysoverlapp.data.db.HolidayDao
import com.example.holidaysoverlapp.data.dto.CountriesDTO
import com.example.holidaysoverlapp.data.dto.HolidaysDTO
import com.example.holidaysoverlapp.util.DataGenerator.getCountries
import com.example.holidaysoverlapp.util.DataGenerator.getCountriesDtos
import com.example.holidaysoverlapp.util.DataGenerator.getHolidays
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class HolidaysRepositoryTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val mockHolidaysService: HolidaysService = mock()
    private val mockCountryDao: CountryDao = mock()
    private val mockHolidayDao: HolidayDao = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when remote api returns success Then repo should also return success with correct mapping`() {
        val countriesDtos = getCountriesDtos()
        val countries = getCountries()

        runTest {
            whenever(mockHolidaysService.getCountries()).thenReturn(CountriesDTO(countriesDtos))
            whenever(mockCountryDao.getAllAsFlow()).thenReturn(flow { emit(countries) })

            val repository = HolidaysRepository(mockHolidaysService, mockCountryDao, mockHolidayDao)

            repository.countriesFlow.collect { countriesList ->
                assertEquals(countriesList.size, countriesDtos.size)
                assertEquals(countriesList.first().code, countriesDtos.first().code)
            }
        }
    }

    @Test
    fun `load holidays clears local holidays when remote response is empty`() {
        val localHolidays = getHolidays()
        val remoteHolidaysDto = HolidaysDTO(emptyList())

        assertEquals(2, localHolidays.size)
        assertEquals(0, remoteHolidaysDto.holidays.size)

        runTest {
            whenever(mockHolidayDao.getAll()).thenReturn(localHolidays)
            whenever(mockHolidaysService.getHolidays(any(), any())).thenReturn(remoteHolidaysDto)

            val repository = HolidaysRepository(mockHolidaysService, mockCountryDao, mockHolidayDao)

            repository.loadHolidays("RS", "AF")

            verify(mockHolidayDao).getAll()
            verify(mockHolidaysService).getHolidays("RS", "2021")
            verify(mockHolidayDao).deleteAllForCountry("RS")

            verify(mockHolidayDao, never()).insert(any())
        }
    }

}