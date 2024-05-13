package com.arch.example.data

import androidx.paging.PagingData
import com.arch.example.entities.photo.Photo
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.Flow

interface TopicRepository {

    fun observeTopics(): Flow<List<Topic>>

    fun fetchAndSaveTopics(order: TopicsOrder): Flow<Unit>

    fun observeTopic(id: String): Flow<Topic>

    fun getTopicPhotos(topicId: String): Flow<PagingData<Photo>>
}