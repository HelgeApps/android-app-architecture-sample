package com.arch.example.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arch.example.designsystem.components.ArchTopAppBar
import com.arch.example.designsystem.components.TopAppBarIcon
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.login.model.LoginUiEffect
import com.arch.example.login.model.LoginUiState
import com.arch.example.translations.R
import com.arch.example.ui.model.SnackbarMessage
import com.arch.example.ui.utils.rememberFlowWithLifecycle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    val scope = rememberCoroutineScope()

    val uiState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val uiEffect = rememberFlowWithLifecycle(loginViewModel.uiEffect)

    LaunchedEffect(uiEffect) {
        uiEffect.collect {
            when (it) {
                is LoginUiEffect.ShowSnackbarError -> {
                    scope.launch {
                        onShowSnackbar(
                            SnackbarMessage(
                                message = it.message,
                                duration = SnackbarDuration.Long
                            )
                        )
                    }
                }
            }
        }
    }

    LoginContent(
        navigateUp = navigateUp,
        uiState = uiState,
    )
}

@Composable
private fun LoginContent(
    uiState: LoginUiState,
    navigateUp: () -> Unit,
) {
    Column {
        ArchTopAppBar(
            title = stringResource(id = R.string.login),
            topAppBarIcon = TopAppBarIcon.BackIcon,
            onNavigationClick = navigateUp
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val currentNavigateUp by rememberUpdatedState(newValue = navigateUp)

            when (uiState) {
                is LoginUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is LoginUiState.Success -> {
                    Text(
                        text = stringResource(R.string.authorized),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    LaunchedEffect(true) {
                        delay(1500L)
                        currentNavigateUp()
                    }
                }

                is LoginUiState.Error -> {
                    Text(
                        modifier = Modifier.padding(24.dp),
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.error
                    )
                    LaunchedEffect(true) {
                        delay(2000L)
                        currentNavigateUp()
                    }
                }
            }
        }
    }
}

@Preview("Light theme")
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPhotosContentSuccess() {
    AppTheme {
        Surface {
            LoginContent(
                uiState = LoginUiState.Success,
                navigateUp = {},
            )
        }
    }
}

@Preview("Light theme")
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPhotosContentLoading() {
    AppTheme {
        Surface {
            LoginContent(
                uiState = LoginUiState.Loading,
                navigateUp = {},
            )
        }
    }
}

@Preview("Light theme")
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPhotosContentError() {
    AppTheme {
        Surface {
            LoginContent(
                uiState = LoginUiState.Error(message = "Auth failed"),
                navigateUp = {},
            )
        }
    }
}
