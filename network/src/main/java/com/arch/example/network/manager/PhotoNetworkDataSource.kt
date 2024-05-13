package com.arch.example.network.manager

import com.arch.example.entities.photo.Photo

interface PhotoNetworkDataSource {

    suspend fun getPhotos(page: Int, perPage: Int): List<Photo>
}