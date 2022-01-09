package com.example.offiqlresturantapp.utils

import java.lang.Exception

sealed class ApisResponse<T>(
    val data: T? = null,
    val exception: Exception? = null
) {
    class Loading<T>(data: T?) : ApisResponse<T>(data)
    class Success<T>(data: T) : ApisResponse<T>(data)
    class Error<T>(data: T? = null, exception: Exception?) : ApisResponse<T>(data, exception)
}

/*sealed class MySealedChannel {
    data class DeleteAndChannel<T>(val userdata: T) : MySealedChannel()
}*/