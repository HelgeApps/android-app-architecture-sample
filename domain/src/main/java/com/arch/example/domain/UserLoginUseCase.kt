package com.arch.example.domain

import com.arch.example.common.result.AsyncResult
import com.arch.example.common.result.asResult
import com.arch.example.data.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(code: String, redirectUri: String): Flow<AsyncResult<Unit>> {
        return authRepository.login(
            code = code,
            redirectUri = redirectUri
        ).asResult()
    }
}
