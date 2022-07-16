package com.example.holidaysoverlapp.data.db

import androidx.room.*
import com.example.holidaysoverlapp.data.entities.Holiday
import kotlinx.coroutines.flow.Flow

@Dao
interface HolidayDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(holidays: List<Holiday>)

    @Query("SELECT * FROM holiday")
    fun getAllAsFlow(): Flow<List<Holiday>>

    @Query("SELECT * FROM holiday")
    suspend fun getAll(): List<Holiday>

    @Query("SELECT * FROM holiday WHERE country = :country")
    suspend fun getAllForCountry(country: String): List<Holiday>

    @Query("SELECT * FROM holiday WHERE uuid = :uuid")
    suspend fun getForId(uuid: String): Holiday?

    @Query("DELETE FROM holiday WHERE country = :country")
    suspend fun deleteAllForCountry(country: String)

    @Delete
    suspend fun delete(holidays: List<Holiday>)

}