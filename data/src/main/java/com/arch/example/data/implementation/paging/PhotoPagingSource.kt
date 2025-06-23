package com.arch.example.data.implementation.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.arch.example.entities.photo.Photo
import com.arch.example.network.manager.PhotoNetworkDataSource
import com.arch.example.network.models.photo.asExternalModel

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class PhotoPagingSource(
    private val photoNetworkDataSource: PhotoNetworkDataSource
) : PagingSource<Int, Photo>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: UNSPLASH_STARTING_PAGE_INDEX
        return try {
            val list = photoNetworkDataSource.getPhotos(page, params.loadSize)
                .map { it.asExternalModel() }
            LoadResult.Page(
                data = list,
                prevKey = if (page == UNSPLASH_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (list.isEmpty()) null else page + 1
            )
        } catch (exception: Throwable) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int {
        return UNSPLASH_STARTING_PAGE_INDEX
    }
}
