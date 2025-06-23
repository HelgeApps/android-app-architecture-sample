package com.arch.example.model

data class MainUiState(
    val isAuthorized: Boolean = false,
    val loading: Boolean = false,
    val userMessage: UserMessage? = null,
)
