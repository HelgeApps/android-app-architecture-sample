package com.arch.example.ui.model

import androidx.compose.material3.SnackbarDuration

data class SnackbarMessage(
    val message: String,
    val duration: SnackbarDuration = SnackbarDuration.Short,
    val action: String? = null,
)
