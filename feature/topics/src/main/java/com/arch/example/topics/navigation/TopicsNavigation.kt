package com.arch.example.topics.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arch.example.topics.TopicsScreen
import com.arch.example.ui.model.SnackbarMessage
import com.arch.example.ui.utils.navigateWithLifecycle
import kotlinx.serialization.Serializable


@Serializable
data object TopicsRoute

fun NavController.navigateToTopics(navOptions: NavOptions? = null) {
    navigateWithLifecycle(TopicsRoute, navOptions)
}

fun NavGraphBuilder.topicsScreen(
    openDrawer: () -> Unit,
    navigateToTopicDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    composable<TopicsRoute> {
        TopicsScreen(
            openDrawer = openDrawer,
            navigateToTopicDetails = navigateToTopicDetails,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
