package com.arch.example.topics.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arch.example.ui.AppDestination
import com.arch.example.ui.utils.navigateWithLifecycle
import com.arch.example.topics.TopicsScreen

object Topics : AppDestination() {
    override val route = "topics"
}

fun NavController.navigateToTopics(navOptions: NavOptions? = null) {
    navigateWithLifecycle(Topics.route, navOptions)
}

fun NavGraphBuilder.topicsScreen(
    openDrawer: () -> Unit,
    navigateToTopicDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean,
) {
    composable(route = Topics.route) {
        TopicsScreen(
            openDrawer = openDrawer,
            navigateToTopicDetails = navigateToTopicDetails,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
