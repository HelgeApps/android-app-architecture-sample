package com.arch.example.network.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService
import com.arch.example.common.network.ErrorGlobalHandlerObserver
import com.arch.example.entities.errors.NetworkException
import com.arch.example.network.R
import com.arch.example.network.models.NetworkError
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class NetworkErrorConverterHelper(
    private val context: Context,
    private val moshi: Moshi
) : ErrorGlobalHandlerObserver {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    private var onUnauthorizedErrorObserver: MutableSharedFlow<Throwable> =
        MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getOnGlobalErrorObserver(): SharedFlow<Throwable> {
        return onUnauthorizedErrorObserver.asSharedFlow()
    }

    private suspend fun send401Error(error: Throwable) {
        onUnauthorizedErrorObserver.emit(error)
    }

    suspend fun parseError(throwable: Throwable): Throwable {
        return if (throwable is HttpException) {
            try {
                val jsonResponse = throwable.response()!!.errorBody()!!.string()
                val parsedError: NetworkError = moshi.adapter(NetworkError::class.java)
                    .fromJson(jsonResponse)!!

                val finalError = when {
                    parsedError.errors != null -> {
                        Exception(parsedError.errors.joinToString("\n"))
                    }

                    !parsedError.error.isNullOrEmpty() -> {
                        Exception(parsedError.error)
                    }

                    else -> {
                        throwable
                    }
                }

                if (throwable.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    send401Error(finalError)
                }

                finalError
            } catch (e: Throwable) {
                val message = context.getString(
                    when (throwable.code()) {
                        HttpURLConnection.HTTP_BAD_REQUEST -> R.string.error_network_default
                        HttpURLConnection.HTTP_FORBIDDEN -> R.string.error_network_forbidden
                        HttpURLConnection.HTTP_UNAUTHORIZED -> R.string.error_network_invalid_session
                        else -> R.string.error_network_default
                    }
                )
                val exception = Exception(message)

                if (throwable.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                    send401Error(exception)
                }

                exception
            }
        } else if (!isNetworkConnected()) {
            NetworkException(context.getString(R.string.error_network_no_internet))
        } else {
            when (throwable) {
                is ConnectException,
                is SocketTimeoutException,
                is UnknownHostException -> Exception(context.getString(R.string.error_network_connection))

                else -> throwable
            }
        }
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager = connectivityManager ?: return false
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            connectivityManager.getNetworkCapabilities(nw)
                ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            @Suppress("DEPRECATION")
            connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }
}
