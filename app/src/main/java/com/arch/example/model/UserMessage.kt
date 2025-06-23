package com.arch.example.model

import androidx.annotation.StringRes

sealed class UserMessage {
    data class Text(val message: String) : UserMessage()
    data class Res(@StringRes val messageRes: Int) : UserMessage()
}
