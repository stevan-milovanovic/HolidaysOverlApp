package com.example.holidaysoverlapp.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Holiday(
    @PrimaryKey
    val uuid: String,
    val name: String,
    val date: String,
    val country: String
) {

    override fun hashCode(): Int {
        return date.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return (other as? Holiday)?.date == date
    }

}