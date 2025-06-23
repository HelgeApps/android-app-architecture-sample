package com.arch.example.login.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import com.arch.example.login.LoginScreen
import com.arch.example.ui.model.SnackbarMessage
import kotlinx.serialization.Serializable

const val unsplashRedirectUri = "unsplash://o_auth"

@Serializable
/**
 *  We set a default value for [code] so it becomes a query-param for our deep link
 */
data class LoginRoute(val code: String = "")

fun NavGraphBuilder.loginScreen(
    navigateUp: () -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    composable<LoginRoute>(
        deepLinks = listOf(
            navDeepLink<LoginRoute>(basePath = unsplashRedirectUri)
        )
    ) {
        LoginScreen(
            navigateUp = navigateUp,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
