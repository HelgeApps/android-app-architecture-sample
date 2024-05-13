package com.arch.example.data.testdoubles

import JvmUnitTestAssetManager
import com.arch.example.common.AppDispatchers
import com.arch.example.common.Dispatcher
import com.arch.example.data.test.TestAssetManager
import com.arch.example.entities.auth.AuthData
import com.arch.example.network.manager.AuthNetworkDataSource
import com.arch.example.network.models.auth.NetworkLoginResponse
import com.arch.example.network.models.auth.asExternalModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source

class TestAuthNetworkDataSource(
    @Dispatcher(AppDispatchers.Default) private val ioDispatcher: CoroutineDispatcher,
    private val moshi: Moshi,
    private val assets: TestAssetManager = JvmUnitTestAssetManager,
) : AuthNetworkDataSource {

    override suspend fun login(code: String, redirectUri: String): AuthData =
        withContext(ioDispatcher) {
            assets.open(AUTH_RESPONSE_ASSET).use {
                moshi.adapter(NetworkLoginResponse::class.java).fromJson(it.source().buffer())!!
            }.asExternalModel()
        }

    override suspend fun refreshAccessToken(refreshToken: String) =
        withContext(ioDispatcher) {
            assets.open(AUTH_RESPONSE_ASSET).use {
                moshi.adapter(NetworkLoginResponse::class.java).fromJson(it.source().buffer())!!
            }.asExternalModel()
        }

    companion object {
        private const val AUTH_RESPONSE_ASSET = "auth_response.json"
    }
}