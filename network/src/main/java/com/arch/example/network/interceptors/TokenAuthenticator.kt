package com.arch.example.network.interceptors

import com.arch.example.datastore.AuthPrefsDataSource
import com.arch.example.entities.auth.AuthPrefsData
import com.arch.example.network.manager.AuthNetworkDataSource
import com.arch.example.network.utils.HEADER_AUTHORIZATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject


class TokenAuthenticator @Inject constructor(
    private val authPrefsDataSource: AuthPrefsDataSource,
    private val authNetworkDataSource: dagger.Lazy<AuthNetworkDataSource>
) : Authenticator {

    private val mutex = Mutex()

    private fun getCurrentAuthData(): AuthPrefsData {
        return runBlocking {
            authPrefsDataSource.authPrefsData().first()
        }
    }

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {

        if (response.request.headers[HEADER_AUTHORIZATION] == null) return@runBlocking null

        val accessToken = getCurrentAuthData().accessToken ?: return@runBlocking null

        mutex.withLock {
            val preferences = getCurrentAuthData()
            val accessTokenAfterWaiting = preferences.accessToken ?: return@runBlocking null
            val tokenType = preferences.tokenType ?: return@runBlocking null

            if (accessToken != accessTokenAfterWaiting) {
                // Access token was refreshed in another async request
                return@runBlocking executeRequestWithNewToken(
                    tokenType = tokenType,
                    accessToken = accessTokenAfterWaiting,
                    response
                )
            }

            val authRepository = authNetworkDataSource.get()

            return@runBlocking try {
                val refreshTokenResponse = authRepository.refreshAccessToken(
                    refreshToken = getCurrentAuthData().refreshToken!!
                )
                executeRequestWithNewToken(
                    tokenType = refreshTokenResponse.tokenType,
                    accessToken = refreshTokenResponse.accessToken,
                    response
                )
            } catch (e: Throwable) {
                e.printStackTrace()
                // Refreshing access token failed
                null
            }
        }
    }

    private fun executeRequestWithNewToken(
        tokenType: String,
        accessToken: String,
        response: Response
    ): Request {
        return response.request.newBuilder()
            .removeHeader(HEADER_AUTHORIZATION)
            .addHeader(HEADER_AUTHORIZATION, "$tokenType $accessToken")
            .build()
    }
}
