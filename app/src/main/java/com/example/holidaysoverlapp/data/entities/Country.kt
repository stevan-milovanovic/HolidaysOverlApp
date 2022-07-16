package com.example.holidaysoverlapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Country(
    @PrimaryKey
    val code: String,
    val name: String,
    val flag: String,
    val selected: Boolean
)