package com.arch.example.network.manager

import com.arch.example.entities.auth.AuthData

interface AuthNetworkDataSource {

    suspend fun login(code: String, redirectUri: String): AuthData

    // TODO: it seems Unsplash doesn't support refresh token logic yet, access token lifetime is 1 year
    suspend fun refreshAccessToken(refreshToken: String): AuthData
}