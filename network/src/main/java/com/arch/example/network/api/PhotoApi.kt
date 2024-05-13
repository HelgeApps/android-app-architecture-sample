package com.arch.example.network.api

import com.arch.example.network.models.photo.NetworkPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotoApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<NetworkPhoto>
}