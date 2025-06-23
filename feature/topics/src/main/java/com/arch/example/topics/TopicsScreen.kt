package com.arch.example.topics

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arch.example.designsystem.components.AppDialogSingleChoice
import com.arch.example.designsystem.components.ArchTopAppBar
import com.arch.example.designsystem.components.TopAppBarIcon
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.topics.model.TopicsUiEffect
import com.arch.example.topics.model.TopicsUiEvent
import com.arch.example.topics.model.TopicsUiState
import com.arch.example.translations.R
import com.arch.example.ui.model.SnackbarMessage
import com.arch.example.ui.utils.rememberFlowWithLifecycle
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun TopicsScreen(
    topicsViewModel: TopicsViewModel = hiltViewModel(),
    openDrawer: () -> Unit,
    navigateToTopicDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var showOrderTypeDialog by rememberSaveable { mutableStateOf(false) }

    val uiState by topicsViewModel.uiState.collectAsStateWithLifecycle()
    val uiEffect = rememberFlowWithLifecycle(topicsViewModel.uiEffect)

    LaunchedEffect(uiEffect) {
        uiEffect.collect { it ->
            when (it) {
                is TopicsUiEffect.ShowSnackbarMessage -> {
                    scope.launch {
                        val actionBtnTapped = onShowSnackbar(
                            SnackbarMessage(
                                message = it.message,
                                duration = SnackbarDuration.Long,
                                action = context.getString(R.string.retry_text)
                            )
                        )
                        if (actionBtnTapped) {
                            topicsViewModel.handleEvent(TopicsUiEvent.LoadTopics)
                        }
                    }
                }
            }
        }
    }

    TopicScreenContent(
        uiState = uiState,
        openDrawer = openDrawer,
        onLoadTopics = {
            topicsViewModel.handleEvent(TopicsUiEvent.LoadTopics)
        },
        navigateToTopicDetails = navigateToTopicDetails,
        onShowOrderTypeDialog = {
            showOrderTypeDialog = true
        },
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
                topicsViewModel.handleEvent(TopicsUiEvent.UpdateTopicOrder(selectedOrder))
            },
            onDismiss = { showOrderTypeDialog = false }
        )
    }
}

@Composable
fun TopicScreenContent(
    modifier: Modifier = Modifier,
    uiState: TopicsUiState,
    openDrawer: () -> Unit,
    onLoadTopics: () -> Unit,
    navigateToTopicDetails: (id: String) -> Unit,
    onShowOrderTypeDialog: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    Column {
        ArchTopAppBar(
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
        PullToRefreshBox(
            modifier = modifier.fillMaxSize(),
            isRefreshing = uiState.loading,
            onRefresh = onLoadTopics,
        ) {
            if (!uiState.loading && uiState.topics.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.topics_empty_placeholder_text),
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            } else {
                TopicList(
                    topics = uiState.topics,
                    navigateToTopicDetails = navigateToTopicDetails
                )
            }
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

@Preview("Light theme")
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopicsContent() {
    AppTheme {
        Surface {
            TopicScreenContent(
                uiState = TopicsUiState(
                    topics = listOf(
                        Topic(
                            id = "0",
                            title = "Topic",
                            description = "Image description",
                            publishedAt = Instant.now(),
                            coverPhoto = null
                        )
                    ),
                    loading = false
                ),
                openDrawer = {},
                onLoadTopics = {},
                navigateToTopicDetails = {},
                onShowOrderTypeDialog = {},
            )
        }
    }
}
