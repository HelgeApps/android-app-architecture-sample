package com.arch.example.model

sealed class MainUiEffect {
    data class ShowToastMessage(val message: UserMessage) : MainUiEffect()
}
