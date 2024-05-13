package com.arch.example.ui.utils

import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

fun NavController.navigateWithLifecycle(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    // In order to discard duplicated navigation events, we check the Lifecycle
    if (currentBackStackEntry?.lifecycleIsResumed() != false) {
        navigate(route, navOptions, navigatorExtras)
    }
}

fun NavController.navigateUpWithLifecycle() {
    // In order to discard duplicated navigation events, we check the Lifecycle
    if (currentBackStackEntry?.lifecycleIsResumed() != false) {
        navigateUp()
    }
}