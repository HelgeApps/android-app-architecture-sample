package com.arch.example.domain

import com.arch.example.common.result.AsyncResult
import com.arch.example.common.result.asResult
import com.arch.example.data.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLogoutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * User logout
     */
    operator fun invoke(withRequest: Boolean = true): Flow<AsyncResult<Unit>> {
        return authRepository.logout(withRequest = withRequest)
            .asResult()
    }
}

