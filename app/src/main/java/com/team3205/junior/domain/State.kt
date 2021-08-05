package com.team3205.junior.domain

sealed class State<out T> {
    object Loading: State<Nothing>()
    object Complete: State<Nothing>()
    data class Error(val exc: Throwable): State<Nothing>()
    data class Success<T>(val data: T): State<T>()
}