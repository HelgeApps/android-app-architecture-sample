package com.arch.example.datastore.implementation

import androidx.datastore.core.DataStore
import com.arch.example.datastore.AuthDataStore
import com.arch.example.entities.auth.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AuthDataStoreImpl(
    private val dataStore: DataStore<AuthData>
) : AuthDataStore {

    override fun authData(): Flow<AuthData> = dataStore.data

    override fun isAuthorized(): Flow<Boolean> =
        authData().map { !it.accessToken.isNullOrEmpty() }.distinctUntilChanged()

    override suspend fun update(authData: AuthData) {
        dataStore.updateData { authData }
    }
}