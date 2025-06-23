package com.arch.example.photos.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.arch.example.photos.PhotosScreen
import com.arch.example.ui.model.SnackbarMessage
import com.arch.example.ui.utils.navigateWithLifecycle
import kotlinx.serialization.Serializable

@Serializable
data object PhotosRoute

fun NavController.navigateToPhotos(navOptions: NavOptions? = null) {
    navigateWithLifecycle(PhotosRoute, navOptions)
}

fun NavGraphBuilder.photosScreen(
    openDrawer: () -> Unit,
    navigateToPhotoDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    composable<PhotosRoute> {
        PhotosScreen(
            openDrawer = openDrawer,
            navigateToPhotoDetails = navigateToPhotoDetails,
            onShowSnackbar = onShowSnackbar,
        )
    }
}
