package com.arch.example.data.implementation

import com.arch.example.datastore.AuthDataStore
import com.arch.example.entities.auth.AuthData
import com.arch.example.network.manager.AuthNetworkDataSource
import com.arch.example.data.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authNetworkDataSource: AuthNetworkDataSource,
    private val authDataStore: AuthDataStore
) : AuthRepository {

    override fun login(code: String, redirectUri: String): Flow<Unit> = flow {
        val authData = authNetworkDataSource.login(code, redirectUri)
        authDataStore.update(authData)
        emit(Unit)
    }

    /**
     * @param withRequest - set it to false in case if we already know that token not valid (on HTTP 401 error code)
     */
    override fun logout(withRequest: Boolean): Flow<Unit> = flow {
        if (!isAuthorized().first()) {
            throw Exception("Was not authorized")
        }
        if (withRequest) {
            // TODO: request to logout (if Unsplash API have it, but keep it for example anyway)
        }
        // reset auth data:
        authDataStore.update(AuthData())
        emit(Unit)
    }

    override fun isAuthorized(): Flow<Boolean> {
        return authDataStore.isAuthorized()
    }

    override fun authData(): Flow<AuthData> {
        return authDataStore.authData()
    }
}