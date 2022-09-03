package com.example.motionlyt.utils

sealed class ResponseWrapper<T>(
    val data: T? = null,
    val exception: Exception? = null
) {
    class Loading<T>(data: T?) : ResponseWrapper<T>(data)
    class Success<T>(data: T) : ResponseWrapper<T>(data)
    class Error<T>(data: T? = null, exception: Exception?) : ResponseWrapper<T>(data, exception)
}