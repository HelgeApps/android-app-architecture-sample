package com.arch.example.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Photo
import androidx.compose.ui.graphics.vector.ImageVector
import com.arch.example.photos.navigation.Photos
import com.arch.example.topics.navigation.Topics
import com.arch.example.translations.R
import com.arch.example.ui.AppDestination

enum class TopLevelDestination(
    val appDestination: AppDestination,
    val icon: ImageVector,
    @StringRes val titleTextId: Int
) {
    TOPICS(
        appDestination = Topics,
        icon = Icons.AutoMirrored.Filled.FormatListBulleted,
        titleTextId = R.string.topics_screen_name
    ),
    PHOTOS(
        appDestination = Photos,
        icon = Icons.Filled.Photo,
        titleTextId = R.string.photos_screen_name
    )
}