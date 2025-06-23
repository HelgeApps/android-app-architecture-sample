package com.arch.example.login.model

sealed class LoginUiEffect {
    data class ShowSnackbarError(val message: String) : LoginUiEffect()
}
