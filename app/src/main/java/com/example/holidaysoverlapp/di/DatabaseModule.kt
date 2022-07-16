package com.example.holidaysoverlapp.di

import android.app.Application
import androidx.room.Room
import com.example.holidaysoverlapp.data.db.CountryDao
import com.example.holidaysoverlapp.data.db.HolidayDao
import com.example.holidaysoverlapp.data.db.HolidaysDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): HolidaysDatabase {
        return Room.databaseBuilder(app, HolidaysDatabase::class.java, "holidays-overlap")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideCountryDao(db: HolidaysDatabase): CountryDao = db.countryDao()

    @Singleton
    @Provides
    fun provideHolidayDao(db: HolidaysDatabase): HolidayDao = db.holidayDao()

}