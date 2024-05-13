package com.arch.example.network.manager.implementation

import com.arch.example.entities.auth.AuthData
import com.arch.example.network.BuildConfig
import com.arch.example.network.api.AuthApi
import com.arch.example.network.manager.AuthNetworkDataSource
import com.arch.example.network.models.auth.asExternalModel
import com.arch.example.network.requests.NetworkLoginRequest
import com.arch.example.network.requests.NetworkRefreshTokenRequest
import com.arch.example.network.utils.NetworkErrorConverterHelper
import com.arch.example.network.utils.OauthGrantType

class AuthNetworkDataSourceImpl(
    private val authApi: AuthApi,
    private val networkErrorConverterHelper: NetworkErrorConverterHelper
) : AuthNetworkDataSource {

    override suspend fun login(code: String, redirectUri: String): AuthData = try {
        authApi.login(
            NetworkLoginRequest(
                clientId = BuildConfig.UNSPLASH_API_ACCESS_KEY,
                clientSecret = BuildConfig.UNSPLASH_API_SECRET_KEY,
                code = code,
                redirectUri = redirectUri,
                grantType = OauthGrantType.AUTHORIZATION_CODE
            )
        ).asExternalModel()
    } catch (e: Throwable) {
        throw networkErrorConverterHelper.parseError(e)
    }

    override suspend fun refreshAccessToken(refreshToken: String) = try {
        authApi.refreshAccessToken(
            NetworkRefreshTokenRequest(
                refreshToken = refreshToken,
                grantType = OauthGrantType.REFRESH_TOKEN
            )
        ).asExternalModel()
    } catch (e: Throwable) {
        throw networkErrorConverterHelper.parseError(e)
    }
}