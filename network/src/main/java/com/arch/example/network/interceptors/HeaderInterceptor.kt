package com.arch.example.network.interceptors

import com.arch.example.network.BuildConfig
import com.arch.example.network.utils.HEADER_AUTHORIZATION
import com.arch.example.network.utils.UNSPLASH_API_VERSION
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response = chain.run {
        val newRequest = request().newBuilder()
            .addHeader("accept", "application/json")
            // Unsplash Developer Access Key https://unsplash.com/documentation#public-authentication
            .addHeader(HEADER_AUTHORIZATION, "Client-ID ${BuildConfig.UNSPLASH_API_ACCESS_KEY}")
            .addHeader("Accept-Version", UNSPLASH_API_VERSION)
            .build()
        proceed(newRequest)
    }
}