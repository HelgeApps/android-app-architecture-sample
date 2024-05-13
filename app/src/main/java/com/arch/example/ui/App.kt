package com.arch.example.ui

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arch.example.designsystem.components.AppDialog
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.navigation.AppNavGraph
import com.arch.example.network.BuildConfig
import com.arch.example.translations.R
import com.arch.example.ui.utils.IntentUtils
import kotlinx.coroutines.launch

@Composable
fun ArchApp() {
    val appViewModel: MainViewModel = hiltViewModel()

    AppTheme {
        Surface {
            val appState = rememberAppState()

            val uiState by appViewModel.uiState.collectAsStateWithLifecycle()

            val context = LocalContext.current

            AppDrawer(
                gesturesEnabled = appState.shouldShowBottomBarAndDrawer,
                drawerState = appState.drawerState,
                coroutineScope = appState.coroutineScope,
                currentAppDestination = appState.currentDestination,
                onTopLevelScreenNavigate = appState::navigateTopLevelScreen,
                isAuthorized = uiState.isAuthorized,
                onLogin = {
                    IntentUtils.openLink(
                        context,
                        context.getString(
                            R.string.unsplash_authorize_link,
                            BuildConfig.UNSPLASH_API_ACCESS_KEY,
                            context.getString(R.string.aouth_login_deep_link)
                        )
                    )
                },
                onLogout = appViewModel::logout
            ) {
                val snackbarHostState = remember { SnackbarHostState() }
                Scaffold(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        if (appState.shouldShowBottomBarAndDrawer) {
                            AppBottomBar(
                                currentAppDestination = appState.currentDestination,
                                onTopLevelScreenNavigate = appState::navigateTopLevelScreen,
                            )
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(snackbarHostState)
                    },
                ) { innerPadding ->
                    AppNavGraph(
                        navController = appState.navController,
                        openDrawer = { appState.coroutineScope.launch { appState.drawerState.open() } },
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                            .windowInsetsPadding(
                                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
                            ),
                        onShowSnackbar = { message, action, duration ->
                            snackbarHostState.currentSnackbarData?.dismiss()
                            snackbarHostState.showSnackbar(
                                message = message,
                                actionLabel = action,
                                duration = duration,
                                withDismissAction = true,
                            ) == SnackbarResult.ActionPerformed
                        }
                    )
                }
            }

            uiState.userMessageDialog?.let { message ->
                AppDialog(
                    title = when (message) {
                        is UserMessage.Res -> context.getString(message.messageRes)
                        is UserMessage.Text -> message.message
                    },
                    onDismiss = appViewModel::dialogMessageShown,
                )
            }

            uiState.userMessageToast?.let { message ->
                LaunchedEffect(true) {
                    Toast.makeText(
                        /* context = */ context,
                        /* text = */ when (message) {
                            is UserMessage.Res -> context.getString(message.messageRes)
                            is UserMessage.Text -> message.message
                        },
                        /* duration = */ Toast.LENGTH_LONG
                    ).show()
                    appViewModel.toastMessageShown()
                }
            }
        }
    }
}