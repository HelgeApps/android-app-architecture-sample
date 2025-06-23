package com.arch.example.topicdetails.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.arch.example.topicdetails.TopicDetailsScreen
import com.arch.example.ui.model.SnackbarMessage
import com.arch.example.ui.utils.navigateWithLifecycle
import kotlinx.serialization.Serializable

@Serializable
data class TopicDetailsRoute(val topicId: String)

fun NavController.navigateToTopicDetails(topicId: String) {
    navigateWithLifecycle(TopicDetailsRoute(topicId))
}

fun NavGraphBuilder.topicDetailsScreen(
    navigateUp: () -> Unit,
    navigateToPhotoDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    composable<TopicDetailsRoute> {
        TopicDetailsScreen(
            navigateUp = navigateUp,
            navigateToPhotoDetails = navigateToPhotoDetails,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
