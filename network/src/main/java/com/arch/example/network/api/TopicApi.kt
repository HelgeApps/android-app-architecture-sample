package com.arch.example.network.api

import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.models.photo.NetworkPhoto
import com.arch.example.network.models.topic.NetworkTopic
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TopicApi {

    @GET("topics")
    suspend fun getTopics(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: TopicsOrder
    ): List<NetworkTopic>

    @GET("topics/{id}/photos")
    suspend fun getTopicPhotos(
        @Path("id") topicId: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<NetworkPhoto>
}