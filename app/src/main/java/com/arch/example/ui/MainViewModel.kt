package com.arch.example.ui

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arch.example.common.network.ErrorGlobalHandlerObserver
import com.arch.example.common.result.AsyncResult
import com.arch.example.common.util.isActive
import com.arch.example.data.AuthRepository
import com.arch.example.domain.UserLogoutUseCase
import com.arch.example.translations.R
import com.arch.example.ui.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isAuthorized: Boolean = false,
    val loading: Boolean = false,
    val userMessageDialog: UserMessage? = null,
    val userMessageToast: UserMessage? = null,
)

sealed class UserMessage {
    data class Text(val message: String) : UserMessage()
    data class Res(@StringRes val messageRes: Int) : UserMessage()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userLogoutUseCase: UserLogoutUseCase,
    authRepository: AuthRepository,
    errorGlobalHandlerObserver: ErrorGlobalHandlerObserver
) : ViewModel() {

    private var logoutJob: Job? = null


    private val _isAuthorized = authRepository.isAuthorized()
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = false
        )
    private val _loading = MutableStateFlow(false)
    private val _userMessageDialog = MutableStateFlow<UserMessage?>(null)
    private val _userMessageToast = MutableStateFlow<UserMessage?>(null)

    val uiState: StateFlow<AuthUiState> = combine(
        _isAuthorized,
        _loading,
        _userMessageDialog,
        _userMessageToast
    ) { isAuthorized, loading, userMessageDialog, userMessageToast ->
        AuthUiState(
            isAuthorized = isAuthorized,
            loading = loading,
            userMessageDialog = userMessageDialog,
            userMessageToast = userMessageToast,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = AuthUiState()
    )

    init {
        viewModelScope.launch {
            errorGlobalHandlerObserver
                .getOnGlobalErrorObserver()
                .collect { error ->
                    autoLogoutOnAuthError(
                        onComplete = {
                            showDialogMessage(UserMessage.Text(error.message ?: error.toString()))
                        }
                    )
                }
        }
    }

    fun logout() {
        if (logoutJob.isActive) return
        logoutJob = viewModelScope.launch {
            userLogoutUseCase().collect { result ->
                when (result) {
                    is AsyncResult.Error -> {
                        _loading.value = false
                        showDialogMessage(UserMessage.Text(result.message))
                    }

                    AsyncResult.Loading -> {
                        _loading.value = true
                    }

                    is AsyncResult.Success -> {
                        _loading.value = false
                        showToastMessage(UserMessage.Res(R.string.logout_success_message))
                    }
                }
            }
        }
    }

    private fun autoLogoutOnAuthError(onComplete: () -> Unit) {
        if (logoutJob.isActive) return
        logoutJob = viewModelScope.launch {
            userLogoutUseCase(withRequest = false).collect {
                if (it is AsyncResult.Success) {
                    onComplete()
                }
            }
        }
    }

    fun dialogMessageShown() {
        _userMessageDialog.value = null
    }

    private fun showDialogMessage(message: UserMessage) {
        _userMessageDialog.value = message
    }

    fun toastMessageShown() {
        _userMessageToast.value = null
    }

    private fun showToastMessage(message: UserMessage) {
        _userMessageToast.value = message
    }
}