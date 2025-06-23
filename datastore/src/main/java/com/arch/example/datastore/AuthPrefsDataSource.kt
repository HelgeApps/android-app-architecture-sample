package com.arch.example.datastore

import com.arch.example.entities.auth.AuthPrefsData
import kotlinx.coroutines.flow.Flow

interface AuthPrefsDataSource {

    fun authPrefsData(): Flow<AuthPrefsData>

    fun isAuthorized(): Flow<Boolean>

    suspend fun update(authPrefsData: AuthPrefsData)
}
