package com.arch.example.datastore.implementation

import androidx.datastore.core.DataStore
import com.arch.example.datastore.AuthPrefsDataSource
import com.arch.example.entities.auth.AuthPrefsData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class AuthPrefsDataSourceImpl(
    private val dataStore: DataStore<AuthPrefsData>
) : AuthPrefsDataSource {

    override fun authPrefsData(): Flow<AuthPrefsData> = dataStore.data

    override fun isAuthorized(): Flow<Boolean> =
        authPrefsData().map { !it.accessToken.isNullOrEmpty() }.distinctUntilChanged()

    override suspend fun update(authPrefsData: AuthPrefsData) {
        dataStore.updateData { authPrefsData }
    }
}