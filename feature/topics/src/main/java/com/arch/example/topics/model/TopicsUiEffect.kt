package com.arch.example.topics.model

sealed class TopicsUiEffect {
    data class ShowSnackbarMessage(val message: String) : TopicsUiEffect()
}
