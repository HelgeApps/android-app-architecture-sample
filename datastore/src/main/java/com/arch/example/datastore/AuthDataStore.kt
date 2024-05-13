package com.arch.example.datastore

import com.arch.example.entities.auth.AuthData
import kotlinx.coroutines.flow.Flow

interface AuthDataStore {

    fun authData(): Flow<AuthData>

    fun isAuthorized(): Flow<Boolean>

    suspend fun update(authData: AuthData)
}