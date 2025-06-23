package com.arch.example.model

sealed class MainUiEvent {
    data object HideDialogMessage : MainUiEvent()
    data object Logout : MainUiEvent()
}