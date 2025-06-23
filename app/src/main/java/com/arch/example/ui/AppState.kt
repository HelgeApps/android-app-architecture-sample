package com.arch.example.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.arch.example.photos.navigation.navigateToPhotos
import com.arch.example.topics.navigation.navigateToTopics
import com.arch.example.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope

/**
 * Remembers and creates an instance of [AppState]
 */
@Composable
fun rememberAppState(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) = remember(drawerState, navController, coroutineScope) {
    AppState(drawerState, navController, coroutineScope)
}

/**
 * Responsible for holding state related to App and containing UI-related logic.
 */
@Stable
class AppState(
    val drawerState: DrawerState,
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {
    // ----------------------------------------------------------
    // BottomBar state source of truth
    // ----------------------------------------------------------

    private val topLevelDestinations = TopLevelDestination.entries

    // Reading this attribute will cause recompositions when the bottom bar needs shown, or not.
    // Not all routes need to show the bottom bar.
    val shouldShowBottomBarAndDrawer: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.let { destination ->
                topLevelDestinations.any { destination.hasRoute(it.appDestination::class) }
            } == true

    // ----------------------------------------------------------
    // Navigation state source of truth
    // ----------------------------------------------------------

    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    fun navigateTopLevelScreen(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.TOPICS -> navController.navigateToTopics(topLevelNavOptions)
            TopLevelDestination.PHOTOS -> navController.navigateToPhotos(topLevelNavOptions)
        }
    }
}
