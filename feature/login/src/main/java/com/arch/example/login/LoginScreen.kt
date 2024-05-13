package com.arch.example.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arch.example.designsystem.components.AppTopAppBar
import com.arch.example.designsystem.components.TopAppBarIcon
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.translations.R
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean,
) {
    val loginUiState by loginViewModel.uiState.collectAsStateWithLifecycle()

    LoginContent(
        navigateUp = navigateUp,
        loginUiState = loginUiState,
        onShowSnackbar = onShowSnackbar
    )
}

@Composable
private fun LoginContent(
    navigateUp: () -> Unit = {},
    loginUiState: LoginUiState,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean = { _, _, _ -> false },
) {
    Column {
        AppTopAppBar(
            title = stringResource(id = R.string.login),
            topAppBarIcon = TopAppBarIcon.BackIcon,
            onNavigationClick = navigateUp
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
        ) {
            val currentNavigateUp by rememberUpdatedState(newValue = navigateUp)

            when (loginUiState) {
                is LoginUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is LoginUiState.Success -> {
                    Text(
                        text = stringResource(R.string.authorized),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    LaunchedEffect(true) {
                        delay(1500L)
                        currentNavigateUp()
                    }
                }

                is LoginUiState.Error -> {
                    LaunchedEffect(true) {
                        onShowSnackbar(loginUiState.message, null, SnackbarDuration.Long)
                    }
                    LaunchedEffect(true) {
                        delay(2000L)
                        currentNavigateUp()
                    }
                }
            }
        }
    }
}

@Preview("Regular colors")
@Preview("Dark colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPhotosContent() {
    AppTheme {
        Surface {
            LoginContent(
                loginUiState = LoginUiState.Success,
            )
        }
    }
}