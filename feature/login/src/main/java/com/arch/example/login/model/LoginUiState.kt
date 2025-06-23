package com.arch.example.login.model

sealed class LoginUiState {
    data object Loading : LoginUiState()
    data object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
