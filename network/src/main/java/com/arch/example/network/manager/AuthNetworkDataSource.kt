package com.arch.example.network.manager

import com.arch.example.network.models.auth.NetworkLoginResponse

interface AuthNetworkDataSource {

    suspend fun login(code: String, redirectUri: String): NetworkLoginResponse

    // TODO: it seems Unsplash doesn't support refresh token logic yet, access token lifetime is 1 year
    suspend fun refreshAccessToken(refreshToken: String): NetworkLoginResponse
}
