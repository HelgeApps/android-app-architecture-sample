package com.arch.example.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted

private const val StopTimeoutMillis: Long = 5000

/**
 * A [SharingStarted] meant to be used with a [StateFlow] to expose data to the UI.
 *
 * When the UI stops observing, upstream flows stay active for some time to allow the system to
 * come back from a short-lived configuration change (such as rotations). If the UI stops
 * observing for longer, the cache is kept but the upstream flows are stopped. When the UI comes
 * back, the latest value is replayed and the upstream flows are executed again. This is done to
 * save resources when the app is in the background but let users switch between apps quickly.
 */
val WhileUiSubscribed: SharingStarted = SharingStarted.WhileSubscribed(StopTimeoutMillis)

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED

fun NavController.navigateWithLifecycle(
    route: Any,
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

/**
 * Remembers in the Composition a flow that only emits data when `lifecycle` is
 * at least in `minActiveState`. That's achieved using the `Flow.flowWithLifecycle` operator.
 *
 * Explanation: If flows with operators in composable functions are not remembered, operators
 * will _always_ be called and applied on every recomposition.
 */
@Composable
fun <T> rememberFlowWithLifecycle(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): Flow<T> = remember(flow, lifecycle) {
    flow.flowWithLifecycle(
        lifecycle = lifecycle,
        minActiveState = minActiveState
    )
}
