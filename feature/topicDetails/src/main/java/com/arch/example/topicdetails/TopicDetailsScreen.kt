package com.arch.example.topicdetails

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.arch.example.designsystem.components.ArchTopAppBar
import com.arch.example.designsystem.components.TopAppBarIcon
import com.arch.example.designsystem.components.pagingLoadStateItem
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.entities.photo.Photo
import com.arch.example.entities.photo.PhotoUrls
import com.arch.example.entities.topic.Topic
import com.arch.example.topicdetails.model.TopicUiState
import com.arch.example.translations.R
import com.arch.example.ui.PhotoListItem
import com.arch.example.ui.model.SnackbarMessage
import com.arch.example.ui.utils.FormatUtils
import kotlinx.coroutines.flow.flowOf
import java.time.Instant

@Composable
fun TopicDetailsScreen(
    topicDetailsViewModel: TopicDetailsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToPhotoDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (SnackbarMessage) -> Boolean,
) {
    val context = LocalContext.current

    val topicUiState by topicDetailsViewModel.topicUiState.collectAsStateWithLifecycle()
    val photosResponse = topicDetailsViewModel.photosPagingFlow.collectAsLazyPagingItems()

    val loadStatus = photosResponse.loadState.refresh
    LaunchedEffect(loadStatus) {
        if (loadStatus is LoadState.Error) {
            val errorMsg =
                loadStatus.error.message ?: context.getString(R.string.unknown_error)
            val snackBarResult = onShowSnackbar(
                SnackbarMessage(
                    message = errorMsg,
                    duration = SnackbarDuration.Long,
                    action = context.getString(R.string.retry_text)
                )
            )
            if (snackBarResult) {
                photosResponse.refresh()
            }
        }
    }

    TopicDetailsContent(
        modifier = Modifier,
        uiState = topicUiState,
        photosResponse = photosResponse,
        navigateUp = navigateUp,
        navigateToPhotoDetails = navigateToPhotoDetails,
    )
}

@Composable
fun TopicDetailsContent(
    modifier: Modifier,
    uiState: TopicUiState,
    photosResponse: LazyPagingItems<Photo>,
    navigateUp: () -> Unit,
    navigateToPhotoDetails: (id: String) -> Unit,
) {
    Column {
        ArchTopAppBar(
            title = if (uiState is TopicUiState.Success) {
                uiState.topic.title
            } else {
                "..."
            },
            topAppBarIcon = TopAppBarIcon.BackIcon,
            onNavigationClick = navigateUp
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .clipToBounds()
                .verticalScroll(rememberScrollState())
        ) {
            when (uiState) {
                is TopicUiState.Success -> {
                    TopicHeader(uiState.topic)
                }

                TopicUiState.Loading -> {
                    val loadingContentDescription = stringResource(
                        id = R.string.content_description_loading
                    )
                    CircularProgressIndicator(
                        modifier = Modifier
                            .semantics { contentDescription = loadingContentDescription }
                            .padding(vertical = 16.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
            TopicPhotos(
                modifier = Modifier.weight(1f),
                photosResponse = photosResponse,
                navigateToPhotoDetails = navigateToPhotoDetails,
            )
        }
    }

}

@Composable
private fun TopicHeader(topic: Topic) {
    Column(
        modifier = Modifier
            .padding(top = 16.dp)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = topic.title,
            style = MaterialTheme.typography.headlineMedium
        )
        topic.description?.let { description ->
            Text(
                text = FormatUtils.removeHtmlTags(description),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
fun TopicPhotos(
    modifier: Modifier = Modifier,
    photosResponse: LazyPagingItems<Photo>,
    navigateToPhotoDetails: (id: String) -> Unit,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        TopicDetailsPhotoList(
            photosResponse = photosResponse,
            navigateToPhotoDetails = navigateToPhotoDetails
        )
        val loadStatus = photosResponse.loadState.refresh
        if (loadStatus is LoadState.NotLoading && photosResponse.itemSnapshotList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.photos_empty_placeholder_text),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(24.dp)
            )
        } else if (loadStatus is LoadState.Loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun TopicDetailsPhotoList(
    photosResponse: LazyPagingItems<Photo>,
    navigateToPhotoDetails: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp)
    ) {
        pagingLoadStateItem(
            loadState = photosResponse.loadState.prepend,
            onRefresh = photosResponse::retry
        )

        items(
            count = photosResponse.itemCount,
            key = photosResponse.itemKey { item -> item.id }
        ) { index ->
            photosResponse[index]?.let { photo ->
                PhotoListItem(
                    photo = photo,
                    modifier = Modifier.height(300.dp),
                    navigateToPhotoDetails = navigateToPhotoDetails
                )
            }
        }

        pagingLoadStateItem(
            loadState = photosResponse.loadState.append,
            onRefresh = photosResponse::retry
        )
    }
}


@Preview("Light theme")
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopicsDetailsContent() {
    AppTheme {
        Surface {
            TopicDetailsContent(
                modifier = Modifier.padding(),
                uiState = TopicUiState.Success(
                    Topic(
                        id = "0",
                        title = "Topic #1",
                        description = "Body...",
                        publishedAt = Instant.now(),
                        coverPhoto = null
                    )
                ),
                photosResponse = flowOf(
                    PagingData.from(
                        (0..5).map {
                            Photo(
                                id = it.toString(),
                                description = "Image $it description",
                                altDescription = null,
                                createdAt = Instant.now(),
                                color = null,
                                photoUrls = PhotoUrls(full = "https://source.unsplash.com/user/c_v_r/100x100"),
                                width = 300,
                                height = 400
                            )
                        },
                        sourceLoadStates = LoadStates(
                            LoadState.NotLoading(true),
                            LoadState.NotLoading(true),
                            LoadState.NotLoading(true)
                        )
                    )
                ).collectAsLazyPagingItems(),
                navigateUp = {},
                navigateToPhotoDetails = {},
            )
        }
    }
}
