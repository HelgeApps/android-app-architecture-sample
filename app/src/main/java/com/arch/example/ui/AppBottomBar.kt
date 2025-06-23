package com.arch.example.ui


import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.arch.example.designsystem.components.AppNavigationBar
import com.arch.example.designsystem.components.AppNavigationBarItem
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.navigation.TopLevelDestination

@Composable
fun AppBottomBar(
    currentAppDestination: NavDestination?,
    onTopLevelScreenNavigate: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    AppNavigationBar(modifier = modifier) {
        TopLevelDestination.entries.forEach { item ->
            AppNavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(item.titleTextId)) },
                selected = currentAppDestination?.hasRoute(item.appDestination::class) == true,
                onClick = { onTopLevelScreenNavigate(item) }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview("Light theme")
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewBottomBar() {
    AppTheme {
        Scaffold(
            bottomBar = {
                AppBottomBar(
                    currentAppDestination = NavDestination("TopicsRoute"),
                    onTopLevelScreenNavigate = {}
                )
            }
        ) {}
    }
}