package com.example.holidaysoverlapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.holidaysoverlapp.data.entities.Country
import com.example.holidaysoverlapp.data.entities.Holiday

@Database(
    entities = [Country::class, Holiday::class],
    version = 3,
    exportSchema = false
)
abstract class HolidaysDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao

    abstract fun holidayDao(): HolidayDao

}