package com.arch.example.network.manager

import com.arch.example.network.models.photo.NetworkPhoto

interface PhotoNetworkDataSource {

    suspend fun getPhotos(page: Int, perPage: Int): List<NetworkPhoto>
}
