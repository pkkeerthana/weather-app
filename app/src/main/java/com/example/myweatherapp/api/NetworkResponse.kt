package com.example.myweatherapp.api

// T refers to weatherModel
sealed class NetworkResponse<out T> {
    data class Success<out T>(val data:T) : NetworkResponse<T>()
    data class Error(val message: String): NetworkResponse<Nothing>()
    object loading : NetworkResponse<Nothing>()
}