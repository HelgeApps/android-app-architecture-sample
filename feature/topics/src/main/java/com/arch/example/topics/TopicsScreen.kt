package com.arch.example.topics

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arch.example.designsystem.components.AppDialogSingleChoice
import com.arch.example.designsystem.components.AppTopAppBar
import com.arch.example.designsystem.components.TopAppBarIcon
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.translations.R
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.Instant

@Composable
fun TopicsScreen(
    topicsViewModel: TopicsViewModel = hiltViewModel(),
    openDrawer: () -> Unit,
    navigateToTopicDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean = { _, _, _ -> false },
) {
    var showOrderTypeDialog by rememberSaveable { mutableStateOf(false) }

    val uiState by topicsViewModel.uiState.collectAsStateWithLifecycle()

    TopicScreenContent(
        topics = uiState.topics,
        loading = uiState.loading,
        userMessage = uiState.userMessage,
        openDrawer = openDrawer,
        onLoadTopics = topicsViewModel::refreshTopics,
        navigateToTopicDetails = navigateToTopicDetails,
        onRefresh = topicsViewModel::refreshTopics,
        onShowOrderTypeDialog = {
            showOrderTypeDialog = true
        },
        onShowSnackbar = onShowSnackbar,
        onUserMessageDisplayed = topicsViewModel::snackbarMessageShown
    )

    if (showOrderTypeDialog) {
        val orders = TopicsOrder.entries

        AppDialogSingleChoice(
            title = stringResource(id = R.string.topics_order_by_text),
            options = orders,
            optionsTitles = remember {
                orders.map { it.name }
            },
            selectedOption = uiState.topicsOrder,
            onPositiveBtnClicked = { selectedOrder ->
                topicsViewModel.updateTopicSorting(selectedOrder)
            },
            onDismiss = { showOrderTypeDialog = false }
        )
    }
}

@Composable
fun TopicScreenContent(
    modifier: Modifier = Modifier,
    topics: List<Topic>,
    loading: Boolean,
    userMessage: String?,
    openDrawer: () -> Unit = {},
    onLoadTopics: () -> Unit = {},
    onUserMessageDisplayed: () -> Unit = {},
    navigateToTopicDetails: (id: String) -> Unit = {},
    onRefresh: () -> Unit = {},
    onShowOrderTypeDialog: () -> Unit = {},
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean = { _, _, _ -> false },
) {
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onRefresh()
        }
    }
    if (!loading && pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            pullToRefreshState.endRefresh()
        }
    }
    Column {
        AppTopAppBar(
            title = stringResource(id = R.string.topics_screen_name),
            topAppBarIcon = TopAppBarIcon.MenuIcon,
            onNavigationClick = { coroutineScope.launch { openDrawer() } },
            actions = {
                IconButton(onClick = onShowOrderTypeDialog) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = stringResource(id = R.string.content_description_order)
                    )
                }
            }
        )
        Box(
            modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .clipToBounds()
        ) {
            if (!loading && topics.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.topics_empty_placeholder_text),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            } else {
                TopicList(
                    topics = topics,
                    navigateToTopicDetails = navigateToTopicDetails
                )
            }

            userMessage?.let {
                val currentOnLoadTopics by rememberUpdatedState(onLoadTopics)
                val currentOnUserMessageDisplayed by rememberUpdatedState(onUserMessageDisplayed)
                LaunchedEffect(true) {
                    val snackBarResult = onShowSnackbar(it, null, SnackbarDuration.Long)
                    if (snackBarResult) {
                        currentOnLoadTopics()
                    }
                    currentOnUserMessageDisplayed()
                }
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
            )
        }
    }
}

@Composable
fun TopicList(
    modifier: Modifier = Modifier,
    topics: List<Topic>,
    navigateToTopicDetails: (id: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(all = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = topics,
            key = { _, topic -> topic.id }
        ) { index, topic ->
            if (index != 0) {
                TopicDivider()
                Spacer(modifier = Modifier.height(16.dp))
            }
            TopicListItem(
                topic = topic,
                navigateToTopicDetails = navigateToTopicDetails
            )
        }
    }
}

@Composable
private fun TopicDivider() {
    HorizontalDivider(
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    )
}

@Preview("Regular colors")
@Preview("Dark colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopicsContent() {
    AppTheme {
        Surface {
            TopicScreenContent(
                loading = false,
                userMessage = null,
                topics = listOf(
                    Topic(
                        id = "0",
                        title = "Topic",
                        description = "Image description",
                        publishedAt = Instant.now(),
                        coverPhoto = null
                    )
                )
            )
        }
    }
}