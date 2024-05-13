package com.arch.example.topicdetails.navigation

import android.net.Uri
import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.arch.example.ui.AppDestination
import com.arch.example.ui.utils.navigateWithLifecycle
import com.arch.example.topicdetails.TopicDetailsScreen

object TopicDetails : AppDestination() {
    const val TOPIC_ID_ARG = "topic_id"

    override val route = "topic_details"
    val routeWithArgs = "$route/{$TOPIC_ID_ARG}"
    val arguments = listOf(
        navArgument(TOPIC_ID_ARG) { type = NavType.StringType }
    )
}

fun NavController.navigateToTopicDetails(topicId: String) {
    val encodedId = Uri.encode(topicId)
    navigateWithLifecycle("${TopicDetails.route}/$encodedId")
}

fun NavGraphBuilder.topicDetailsScreen(
    navigateUp: () -> Unit,
    navigateToPhotoDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean,
) {
    composable(
        route = TopicDetails.routeWithArgs,
        arguments = TopicDetails.arguments
    ) {
        TopicDetailsScreen(
            navigateUp = navigateUp,
            navigateToPhotoDetails = navigateToPhotoDetails,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
