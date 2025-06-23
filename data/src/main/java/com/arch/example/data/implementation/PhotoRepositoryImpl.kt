package com.arch.example.data.implementation

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arch.example.entities.photo.Photo
import com.arch.example.network.manager.PhotoNetworkDataSource
import com.arch.example.network.utils.PAGE_SIZE
import com.arch.example.data.PhotoRepository
import com.arch.example.data.implementation.paging.PhotoPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class PhotoRepositoryImpl @Inject constructor(
    private val photoNetworkDataSource: PhotoNetworkDataSource
) : PhotoRepository {

    override fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 2
            ),
            pagingSourceFactory = { PhotoPagingSource(photoNetworkDataSource) }
        ).flow
    }
}