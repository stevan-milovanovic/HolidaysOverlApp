package com.example.holidaysoverlapp.ui

import app.cash.turbine.test
import com.example.holidaysoverlapp.data.HolidaysRepository
import com.example.holidaysoverlapp.ui.util.EntityListViewState
import com.example.holidaysoverlapp.ui.util.ErrorType
import com.example.holidaysoverlapp.util.DataGenerator.getCountries
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val mockRepository: HolidaysRepository = mock()

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Given collections are loaded When data source is success Then emit success view state`() {
        val expectedCountries = getCountries()
        runTest {
            val testFlow = flow { emit(expectedCountries) }
            whenever(mockRepository.countriesFlow).thenReturn(testFlow)

            HomeViewModel(mockRepository).countryViewState.test {
                assertEquals(awaitItem(), EntityListViewState.Loading)
                assertEquals(awaitItem(), EntityListViewState.Success(expectedCountries))
            }
        }
    }

    @Test
    fun `Connection error emits connection error view state`() {
        runTest {
            whenever(mockRepository.countriesFlow).thenReturn(emptyFlow())
            whenever(mockRepository.loadCountries()).doAnswer {
                throw UnknownHostException("Test exception")
            }
            HomeViewModel(mockRepository).countryViewState.test {
                assertEquals(awaitItem(), EntityListViewState.Loading)
                assertEquals(awaitItem(), EntityListViewState.Error(ErrorType.NoInternet))
            }
        }
    }

}