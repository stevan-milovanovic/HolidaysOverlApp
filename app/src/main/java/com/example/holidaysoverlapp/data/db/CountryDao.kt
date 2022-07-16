package com.example.holidaysoverlapp.data.db

import androidx.room.*
import com.example.holidaysoverlapp.data.entities.Country
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(countries: List<Country>)

    @Query("SELECT * FROM country")
    fun getAllAsFlow(): Flow<List<Country>>

    @Update
    suspend fun updateSelectedState(country: Country)

    @Query("SELECT * FROM country")
    suspend fun getAll(): List<Country>

    @Query("SELECT * FROM country WHERE selected = 1")
    suspend fun getSelected(): List<Country>

    @Query("SELECT * FROM country WHERE code = :code")
    suspend fun getForCode(code: String): Country?

    @Query("DELETE FROM country")
    suspend fun deleteAll()

    @Delete
    suspend fun delete(countries: List<Country>)

}