package com.arch.example.photos

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.arch.example.designsystem.components.AppTopAppBar
import com.arch.example.designsystem.components.TopAppBarIcon
import com.arch.example.designsystem.components.items
import com.arch.example.designsystem.components.pagingLoadStateItem
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.entities.photo.Photo
import com.arch.example.entities.photo.PhotoUrls
import com.arch.example.translations.R
import com.arch.example.ui.PhotoListItem
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import java.time.Instant

@Composable
fun PhotosScreen(
    photosViewModel: PhotosViewModel = hiltViewModel(),
    openDrawer: () -> Unit,
    navigateToPhotoDetails: (id: String) -> Unit,
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean,
) {
    val photosResponse = photosViewModel.photosResponse.collectAsLazyPagingItems()

    PhotoScreenContent(
        photosResponse = photosResponse,
        openDrawer = openDrawer,
        navigateToPhotoDetails = navigateToPhotoDetails,
        onShowSnackbar = onShowSnackbar,
    )
}

@Composable
fun PhotoScreenContent(
    modifier: Modifier = Modifier,
    photosResponse: LazyPagingItems<Photo>,
    navigateToPhotoDetails: (id: String) -> Unit = {},
    openDrawer: () -> Unit = {},
    onShowSnackbar: suspend (message: String, action: String?, duration: SnackbarDuration) -> Boolean = { _, _, _ -> false },
) {
    val coroutineScope = rememberCoroutineScope()
    val loadStatus = photosResponse.loadState.refresh

    val pullToRefreshState = rememberPullToRefreshState()
    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            photosResponse.refresh()
        }
    }
    if (loadStatus !is LoadState.Loading && pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            pullToRefreshState.endRefresh()
        }
    }
    Column {
        AppTopAppBar(
            title = stringResource(id = R.string.photos_screen_name),
            topAppBarIcon = TopAppBarIcon.MenuIcon,
            onNavigationClick = { coroutineScope.launch { openDrawer() } }
        )
        Box(
            modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .clipToBounds()
        ) {
            PhotoList(
                photosResponse = photosResponse,
                navigateToPhotoDetails = navigateToPhotoDetails
            )

            if (loadStatus is LoadState.NotLoading) {
                if (photosResponse.itemSnapshotList.isEmpty()) {
                    Text(
                        text = stringResource(id = R.string.photos_empty_placeholder_text),
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(24.dp)
                    )
                }
            } else if (loadStatus is LoadState.Error) {
                val errorMsg =
                    loadStatus.error.message ?: stringResource(id = R.string.unknown_error)
                LaunchedEffect(true) {
                    val snackBarResult = onShowSnackbar(errorMsg, null, SnackbarDuration.Long)
                    if (snackBarResult) {
                        photosResponse.refresh()
                    }
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
fun PhotoList(
    photosResponse: LazyPagingItems<Photo>,
    navigateToPhotoDetails: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cellsCount = 2
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(cellsCount),
        modifier = modifier.padding(8.dp)
    ) {
        pagingLoadStateItem(
            loadState = photosResponse.loadState.prepend,
            onRefresh = photosResponse::retry
        )

        items(photosResponse) { photo ->
            photo?.let {
                PhotoListItem(
                    photo = photo,
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

@Preview("Regular colors")
@Preview("Dark colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPhotosContent() {
    AppTheme {
        Surface {
            PhotoScreenContent(
                photosResponse = flowOf(
                    PagingData.from(
                        listOf(
                            Photo(
                                id = "0",
                                description = "Image description",
                                altDescription = null,
                                createdAt = Instant.now(),
                                color = null,
                                photoUrls = PhotoUrls(full = "https://source.unsplash.com/user/c_v_r/100x100"),
                                width = 300,
                                height = 400
                            )
                        ),
                        sourceLoadStates = LoadStates(
                            LoadState.NotLoading(true),
                            LoadState.NotLoading(true),
                            LoadState.NotLoading(true)
                        )
                    )
                ).collectAsLazyPagingItems(),
            )
        }
    }
}