package com.arch.example.designsystem.components

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.translations.R

sealed class TopAppBarIcon {
    data object NoneIcon : TopAppBarIcon()
    data object BackIcon : TopAppBarIcon()
    data object MenuIcon : TopAppBarIcon()
    abstract class CustomIcon : TopAppBarIcon() {
        @Composable
        abstract fun Icon()
    }
}

@Composable
fun ArchTopAppBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    topAppBarIcon: TopAppBarIcon = TopAppBarIcon.NoneIcon,
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(),
    onNavigationClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            title?.let {
                Text(text = title)
            }
        },
        navigationIcon = {
            ToolbarIcon(
                topAppBarIcon = topAppBarIcon,
                onIconClicked = onNavigationClick
            )
        },
        actions = actions,
        colors = colors,
        modifier = modifier
    )
}

@Composable
fun ToolbarIcon(
    topAppBarIcon: TopAppBarIcon = TopAppBarIcon.NoneIcon,
    onIconClicked: () -> Unit
) {
    when (topAppBarIcon) {
        TopAppBarIcon.NoneIcon -> {
            // without icon
        }

        TopAppBarIcon.BackIcon -> {
            IconButton(onClick = { onIconClicked() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.content_description_drawer_button)
                )
            }
        }

        TopAppBarIcon.MenuIcon -> {
            IconButton(onClick = { onIconClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.content_description_navigate_up)
                )
            }
        }

        is TopAppBarIcon.CustomIcon -> {
            IconButton(onClick = { onIconClicked() }) {
                topAppBarIcon.Icon()
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview("Light theme", heightDp = 300)
@Preview("Dark theme", heightDp = 300, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewToolbar() {
    AppTheme {
        Scaffold(
            topBar = {
                ArchTopAppBar(
                    title = stringResource(id = R.string.topics_screen_name),
                    topAppBarIcon = TopAppBarIcon.MenuIcon
                )
            }
        ) {}
    }
}