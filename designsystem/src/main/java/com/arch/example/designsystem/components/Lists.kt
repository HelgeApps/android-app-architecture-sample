package com.arch.example.designsystem.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.arch.example.translations.R

fun LazyListScope.pagingLoadStateItem(
    loadState: LoadState,
    onRefresh: () -> Unit
) {
    if (loadState == LoadState.Loading) {
        item {
            PagingLoadState()
        }
    } else if (loadState is LoadState.Error) {
        item {
            PagingErrorState(
                loadState = loadState,
                onRefresh = onRefresh
            )
        }
    }
}

fun LazyStaggeredGridScope.pagingLoadStateItem(
    loadState: LoadState,
    onRefresh: () -> Unit
) {
    if (loadState == LoadState.Loading) {
        item(span = StaggeredGridItemSpan.FullLine) {
            PagingLoadState()
        }
    } else if (loadState is LoadState.Error) {
        item(span = StaggeredGridItemSpan.FullLine) {
            PagingErrorState(
                loadState = loadState,
                onRefresh = onRefresh
            )
        }
    }
}

fun LazyGridScope.pagingLoadStateItem(
    loadState: LoadState,
    cellsCount: Int,
    onRefresh: () -> Unit
) {
    if (loadState == LoadState.Loading) {
        item(span = { GridItemSpan(cellsCount) }) {
            PagingLoadState()
        }
    } else if (loadState is LoadState.Error) {
        item(span = { GridItemSpan(cellsCount) }) {
            PagingErrorState(
                loadState = loadState,
                onRefresh = onRefresh
            )
        }
    }
}

@Composable
fun PagingLoadState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun PagingErrorState(
    loadState: LoadState.Error,
    onRefresh: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = loadState.error.message ?: stringResource(id = R.string.unknown_error),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center
        )
        Button(onClick = onRefresh, modifier = Modifier.padding(top = 8.dp)) {
            Text(text = stringResource(id = R.string.retry_text))
        }
    }
}

fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

fun <T : Any> LazyStaggeredGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    key: ((index: Int) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    itemContent: @Composable LazyStaggeredGridItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount, key, contentType) { index ->
        itemContent(lazyPagingItems[index])
    }
}
