package com.arch.example.network.manager.implementation

import com.arch.example.entities.photo.Photo
import com.arch.example.network.api.PhotoApi
import com.arch.example.network.manager.PhotoNetworkDataSource
import com.arch.example.network.models.photo.asExternalModel
import com.arch.example.network.utils.NetworkErrorConverterHelper

class PhotoNetworkDataSourceImpl(
    private val photoApi: PhotoApi,
    private val networkErrorConverterHelper: NetworkErrorConverterHelper
) : PhotoNetworkDataSource {

    override suspend fun getPhotos(page: Int, perPage: Int): List<Photo> = try {
        val data = photoApi.getPhotos(page, perPage)
        data.map { it.asExternalModel() }
    } catch (e: Throwable) {
        throw networkErrorConverterHelper.parseError(e)
    }
}