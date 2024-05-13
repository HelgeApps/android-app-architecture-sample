package com.arch.example.photos.navigation

import androidx.compose.material3.SnackbarDuration
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arch.example.ui.AppDestination
import com.arch.example.ui.utils.navigateWithLifecycle
import com.arch.example.photos.PhotosScreen

object Photos : AppDestination() {
    override val route = "photos"
}

fun NavController.navigateToPhotos(navOptions: NavOptions? = null) {
    navigateWithLifecycle(Photos.route, navOptions)
}

fun NavGraphBuilder.photosScreen(
    openDrawer: () -> Unit,
    navigateToPhotoDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean,
) {
    composable(route = Photos.route) {
        PhotosScreen(
            openDrawer = openDrawer,
            navigateToPhotoDetails = navigateToPhotoDetails,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
