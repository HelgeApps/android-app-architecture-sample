package com.arch.example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arch.example.common.network.ErrorGlobalHandlerObserver
import com.arch.example.common.result.AsyncResult
import com.arch.example.common.util.isActive
import com.arch.example.data.AuthRepository
import com.arch.example.domain.UserLogoutUseCase
import com.arch.example.model.MainUiEffect
import com.arch.example.model.MainUiEvent
import com.arch.example.model.MainUiState
import com.arch.example.model.UserMessage
import com.arch.example.translations.R
import com.arch.example.ui.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userLogoutUseCase: UserLogoutUseCase,
    authRepository: AuthRepository,
    errorGlobalHandlerObserver: ErrorGlobalHandlerObserver
) : ViewModel() {

    private var logoutJob: Job? = null

    private val isAuthorized = authRepository.isAuthorized()
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = false
        )
    private val loading = MutableStateFlow(false)
    private val dialogMessage = MutableStateFlow<UserMessage?>(null)

    val uiState: StateFlow<MainUiState> = combine(
        isAuthorized,
        loading,
        dialogMessage,
    ) { isAuthorized, loading, dialogMessage ->
        MainUiState(
            isAuthorized = isAuthorized,
            loading = loading,
            userMessage = dialogMessage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = MainUiState()
    )

    private val _uiEffect = Channel<MainUiEffect>(Channel.UNLIMITED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            errorGlobalHandlerObserver
                .getOnGlobalErrorObserver()
                .collect { error ->
                    val errorMessage = error.message ?: error.toString()
                    autoLogoutOnAuthError(
                        onComplete = {
                            showDialogMessage(UserMessage.Text(errorMessage))
                        }
                    )
                }
        }
    }

    fun handleEvent(event: MainUiEvent) {
        when (event) {
            MainUiEvent.HideDialogMessage -> {
                hideDialogMessage()
            }

            MainUiEvent.Logout -> {
                logout()
            }
        }
    }

    private fun sendEffect(effect: MainUiEffect) {
        _uiEffect.trySend(effect)
    }

    private fun hideDialogMessage() {
        dialogMessage.value = null
    }

    private fun showDialogMessage(message: UserMessage) {
        dialogMessage.value = message
    }

    private fun logout() {
        if (logoutJob.isActive) {
            return
        }
        logoutJob = viewModelScope.launch {
            userLogoutUseCase().collect { result ->
                when (result) {
                    is AsyncResult.Error -> {
                        loading.value = false
                        showDialogMessage(UserMessage.Text(result.message))
                    }

                    AsyncResult.Loading -> {
                        loading.value = true
                    }

                    is AsyncResult.Success -> {
                        loading.value = false
                        sendEffect(
                            MainUiEffect.ShowToastMessage(
                                UserMessage.Res(R.string.logout_success_message)
                            )
                        )
                    }
                }
            }
        }
    }

    private fun autoLogoutOnAuthError(onComplete: () -> Unit) {
        if (logoutJob.isActive) {
            return
        }
        logoutJob = viewModelScope.launch {
            userLogoutUseCase(withRequest = false).collect {
                if (it is AsyncResult.Success) {
                    onComplete()
                }
            }
        }
    }
}
