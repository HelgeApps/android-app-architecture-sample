package com.arch.example.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import timber.log.Timber

/**
 * A generic class that holds a loading signal or the result of an async operation.
 */
sealed class AsyncResult<out R> {
    data object Loading : AsyncResult<Nothing>()
    data class Success<out T>(val data: T) : AsyncResult<T>()
    data class Error(val exception: Throwable) : AsyncResult<Nothing>() {
        val message: String
            get() = exception.message ?: exception.toString()
    }

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=${exception.message}]"
            Loading -> "Loading"
        }
    }
}

inline fun <reified T> Flow<T>.asResult(): Flow<AsyncResult<T>> {
    return this
        .map<T, AsyncResult<T>> {
            AsyncResult.Success(it)
        }
        .onStart { emit(AsyncResult.Loading) }
        .catch { error ->
            Timber.e(error, "toAsyncResult")
            emit(AsyncResult.Error(error))
        }
}
