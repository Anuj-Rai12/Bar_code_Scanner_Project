package com.example.mpos.utils

import kotlinx.coroutines.flow.*


inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline shouldFetch: (ResultType) -> Boolean //= { true }
) = flow {
    val data = query().first()
    val flow = if (shouldFetch(data)) {
        emit(ApisResponse.Loading(data))
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