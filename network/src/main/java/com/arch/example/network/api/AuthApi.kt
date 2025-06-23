package com.arch.example.network.api

import com.arch.example.network.models.auth.NetworkLoginResponse
import com.arch.example.network.requests.NetworkLoginRequest
import com.arch.example.network.requests.NetworkRefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("https://unsplash.com/oauth/token")
    suspend fun login(@Body networkLoginRequest: NetworkLoginRequest): NetworkLoginResponse

    @POST("https://unsplash.com/oauth/token")
    suspend fun refreshAccessToken(
        @Body networkRefreshTokenRequest: NetworkRefreshTokenRequest
    ): NetworkLoginResponse
}