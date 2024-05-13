package com.arch.example.login.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.arch.example.ui.AppDestination
import com.arch.example.login.LoginScreen

object Login : AppDestination() {
    override val route = "login"
    const val unsplashRedirectUri = "unsplash://o_auth"
    const val codeArg = "code"
    val routeWithArgs = "$route?$codeArg={$codeArg}"
    val deepLinks = listOf(
        navDeepLink { uriPattern = "$unsplashRedirectUri?$codeArg={$codeArg}" }
    )
}

fun NavGraphBuilder.loginScreen(
    navigateUp: () -> Unit,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean,
) {
    composable(
        route = Login.routeWithArgs,
        arguments = listOf(navArgument(Login.codeArg) { type = NavType.StringType }),
        deepLinks = Login.deepLinks
    ) {
        LoginScreen(
            navigateUp = navigateUp,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
