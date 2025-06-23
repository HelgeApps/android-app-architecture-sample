package com.arch.example.network.manager

import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.models.photo.NetworkPhoto
import com.arch.example.network.models.topic.NetworkTopic

interface TopicNetworkDataSource {

    suspend fun getTopics(page: Int, perPage: Int, orderBy: TopicsOrder): List<NetworkTopic>

    suspend fun getTopicPhotos(topicId: String, page: Int, perPage: Int): List<NetworkPhoto>
}
