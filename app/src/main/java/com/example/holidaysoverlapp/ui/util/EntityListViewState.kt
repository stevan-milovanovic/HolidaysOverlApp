package com.example.holidaysoverlapp.ui.util

sealed class EntityListViewState<out T : Any> {
    object Loading : EntityListViewState<Nothing>()
    data class Error(val errorType: ErrorType) : EntityListViewState<Nothing>()
    data class Success<out T : Any>(val data: List<T>) : EntityListViewState<T>()
}