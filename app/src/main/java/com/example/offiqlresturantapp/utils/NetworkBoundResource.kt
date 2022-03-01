package com.example.offiqlresturantapp.utils

import kotlinx.coroutines.flow.*


inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean //= { true }
) = flow {
    val data = query().first()
    val flow = if (shouldFetch(data)) {
        emit(ApisResponse.Loading("Please Wait"))
        try {
            saveFetchResult(fetch())
            query().map { ApisResponse.Success(it) }
        } catch (throwable: Exception) {
            query().map { ApisResponse.Error(null, throwable) }
        }
    } else {
        query().map { ApisResponse.Success(it) }
    }
    emitAll(flow)
}