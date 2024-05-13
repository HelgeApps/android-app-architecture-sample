package com.arch.example.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arch.example.common.result.AsyncResult
import com.arch.example.domain.UserLoginUseCase
import com.arch.example.login.navigation.Login
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val unsplashAuthCode = savedStateHandle.get<String>(Login.codeArg)!!

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        login()
    }

    private fun login() {
        viewModelScope.launch {
            userLoginUseCase(
                code = unsplashAuthCode,
                redirectUri = Login.unsplashRedirectUri
            ).collect { result ->
                _uiState.emit(
                    when (result) {
                        AsyncResult.Loading -> LoginUiState.Loading
                        is AsyncResult.Success -> LoginUiState.Success
                        is AsyncResult.Error -> LoginUiState.Error(result.message)
                    }
                )
            }
        }
    }
}

sealed class LoginUiState {
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}