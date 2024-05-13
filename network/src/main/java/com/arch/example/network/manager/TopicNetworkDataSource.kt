package com.arch.example.network.manager

import com.arch.example.entities.photo.Photo
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder

interface TopicNetworkDataSource {

    suspend fun getTopics(page: Int, perPage: Int, orderBy: TopicsOrder): List<Topic>

    suspend fun getTopicPhotos(topicId: String, page: Int, perPage: Int): List<Photo>
}