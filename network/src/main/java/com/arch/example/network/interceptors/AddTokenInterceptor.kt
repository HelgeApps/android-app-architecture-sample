package com.arch.example.network.interceptors

import com.arch.example.datastore.AuthPrefsDataSource
import com.arch.example.network.utils.HEADER_AUTHORIZATION
import com.arch.example.network.utils.HEADER_NO_AUTHENTICATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AddTokenInterceptor @Inject constructor(
    private val authPrefsDataSource: AuthPrefsDataSource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val noAuth = request.header(HEADER_NO_AUTHENTICATION)?.toBoolean() == true
        if (!noAuth) {
            val (token, tokenType, _) = runBlocking { authPrefsDataSource.authPrefsData().first() }
            token?.let {
                request = request.newBuilder()
                    .addHeader(HEADER_AUTHORIZATION, "$tokenType $it")
                    .build()
            }
        }
        return chain.proceed(request)
    }
}