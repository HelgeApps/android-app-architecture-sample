package com.arch.example.data

import com.arch.example.entities.auth.AuthPrefsData
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login(code: String, redirectUri: String): Flow<Unit>

    fun logout(withRequest: Boolean): Flow<Unit>

    fun isAuthorized(): Flow<Boolean>

    fun authData(): Flow<AuthPrefsData>
}
