package com.arch.example.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.arch.example.ui.utils.navigateUpWithLifecycle
import com.arch.example.login.navigation.loginScreen
import com.arch.example.photos.navigation.photosScreen
import com.arch.example.topicdetails.navigation.navigateToTopicDetails
import com.arch.example.topicdetails.navigation.topicDetailsScreen
import com.arch.example.topics.navigation.Topics
import com.arch.example.topics.navigation.topicsScreen


@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = Topics.route,
    openDrawer: () -> Unit = {},
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
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

