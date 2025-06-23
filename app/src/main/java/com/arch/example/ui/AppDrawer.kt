package com.arch.example.ui


import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Android
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.translations.R
import com.arch.example.navigation.TopLevelDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    gesturesEnabled: Boolean = true,
    currentAppDestination: NavDestination?,
    onTopLevelScreenNavigate: (TopLevelDestination) -> Unit,
    isAuthorized: Boolean,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        gesturesEnabled = gesturesEnabled,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawerContent(
                    currentAppDestination = currentAppDestination,
                    onTopLevelScreenNavigate = onTopLevelScreenNavigate,
                    isAuthorized = isAuthorized,
                    onLogin = onLogin,
                    onLogout = onLogout,
                    closeDrawer = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        },
        content = content
    )
}

@Composable
fun AppDrawerContent(
    currentAppDestination: NavDestination?,
    onTopLevelScreenNavigate: (TopLevelDestination) -> Unit,
    isAuthorized: Boolean,
    onLogin: () -> Unit,
    onLogout: () -> Unit,
    closeDrawer: () -> Unit
) {
    // Use windowInsetsTopHeight() to add a spacer which pushes the drawer content
    // below the status bar (y-axis)
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.statusBars))
        Spacer(Modifier.height(8.dp))
        Image(
            imageVector = Icons.Filled.Android,
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .size(96.dp)
                .padding(16.dp)
        )
        AppDrawerDivider()
        TopLevelDestination.entries.forEach { item ->
            DrawerButton(
                icon = item.icon,
                label = stringResource(id = item.titleTextId),
                isSelected = currentAppDestination?.hasRoute(item.appDestination::class) == true,
                action = {
                    onTopLevelScreenNavigate(item)
                    closeDrawer()
                }
            )
        }
        AppDrawerDivider()
        if (!isAuthorized) {
            DrawerButton(
                icon = Icons.AutoMirrored.Filled.Login,
                label = stringResource(id = R.string.login),
                isSelected = false,
                action = {
                    onLogin()
                    closeDrawer()
                }
            )
        } else {
            DrawerButton(
                icon = Icons.AutoMirrored.Filled.Logout,
                label = stringResource(id = R.string.logout),
                isSelected = false,
                action = {
                    onLogout()
                    closeDrawer()
                }
            )
        }
    }
}

@Composable
private fun AppDrawerDivider() {
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = .2f))
}

@Composable
private fun DrawerButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    action: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val imageAlpha = if (isSelected) {
        1f
    } else {
        0.6f
    }
    val textIconColor = if (isSelected) {
        colors.primary
    } else {
        colors.onSurface.copy(alpha = 0.6f)
    }
    val backgroundColor = if (isSelected) {
        colors.primary.copy(alpha = 0.12f)
    } else {
        Color.Transparent
    }

    val surfaceModifier = modifier
        .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        .fillMaxWidth()
    Surface(
        modifier = surfaceModifier,
        color = backgroundColor,
        shape = MaterialTheme.shapes.small
    ) {
        TextButton(
            onClick = action,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    imageVector = icon,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(textIconColor),
                    alpha = imageAlpha
                )
                Spacer(Modifier.width(16.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    color = textIconColor
                )
            }
        }
    }
}

@Preview("Drawer contents")
@Preview("Drawer contents (dark)", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewAppDrawer() {
    AppTheme {
        Surface {
            AppDrawer(
                currentAppDestination = NavDestination("TopicsRoute"),
                onTopLevelScreenNavigate = {},
                isAuthorized = false,
                onLogin = {},
                onLogout = {},
                content = {}
            )
        }
    }
}
