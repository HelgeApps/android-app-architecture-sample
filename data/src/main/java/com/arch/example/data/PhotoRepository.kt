package com.arch.example.data

import androidx.paging.PagingData
import com.arch.example.entities.photo.Photo
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {

    fun getPhotos(): Flow<PagingData<Photo>>
}