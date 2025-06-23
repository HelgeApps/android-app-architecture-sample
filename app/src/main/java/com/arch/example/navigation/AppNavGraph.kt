package com.arch.example.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.arch.example.login.navigation.loginScreen
import com.arch.example.photos.navigation.photosScreen
import com.arch.example.topicdetails.navigation.navigateToTopicDetails
import com.arch.example.topicdetails.navigation.topicDetailsScreen
import com.arch.example.topics.navigation.TopicsRoute
import com.arch.example.topics.navigation.topicsScreen
import com.arch.example.ui.model.SnackbarMessage
import com.arch.example.ui.utils.navigateUpWithLifecycle


@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Any = TopicsRoute,
    openDrawer: () -> Unit = {},
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        topicsScreen(
            openDrawer = openDrawer,
            navigateToTopicDetails = navController::navigateToTopicDetails,
            onShowSnackbar = onShowSnackbar,
        )
        photosScreen(
            openDrawer = openDrawer,
            navigateToPhotoDetails = {}, // TODO: implement photo details screen
            onShowSnackbar = onShowSnackbar,
        )
        topicDetailsScreen(
            navigateUp = navController::navigateUpWithLifecycle,
            navigateToPhotoDetails = {}, // TODO: implement photo details screen
            onShowSnackbar = onShowSnackbar,
        )
        loginScreen(
            navigateUp = navController::navigateUpWithLifecycle,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
