package com.arch.example.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.arch.example.common.result.AsyncResult
import com.arch.example.domain.UserLoginUseCase
import com.arch.example.login.model.LoginUiEffect
import com.arch.example.login.model.LoginUiState
import com.arch.example.login.navigation.LoginRoute
import com.arch.example.login.navigation.unsplashRedirectUri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userLoginUseCase: UserLoginUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val unsplashAuthCode = savedStateHandle.toRoute<LoginRoute>().code

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiEffect = Channel<LoginUiEffect>(Channel.UNLIMITED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        login()
    }

    private fun login() {
        viewModelScope.launch {
            userLoginUseCase(
                code = unsplashAuthCode,
                redirectUri = unsplashRedirectUri
            ).collect { result ->
                _uiState.emit(
                    when (result) {
                        AsyncResult.Loading -> LoginUiState.Loading
                        is AsyncResult.Success -> LoginUiState.Success
                        is AsyncResult.Error -> LoginUiState.Error(result.message).also {
                            // we display the error in Text on UI, and also as Snackbar to
                            // demonstrate both state and effect of MVI pattern
                            sendEffect(LoginUiEffect.ShowSnackbarError(result.message))
                        }
                    }
                )
            }
        }
    }

    private fun sendEffect(effect: LoginUiEffect) {
        _uiEffect.trySend(effect)
    }
}
