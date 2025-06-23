package com.arch.example.testing.repository

import androidx.paging.PagingData
import com.arch.example.data.TopicRepository
import com.arch.example.entities.photo.Photo
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.testing.data.topicPhotosTestData
import com.arch.example.testing.data.topicsTestData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class TestTopicRepository : TopicRepository {
    /**
     * The backing hot flow for the list of topics ids for testing.
     */
    private val topicsFlow: MutableSharedFlow<List<Topic>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun observeTopics(): Flow<List<Topic>> = topicsFlow

    override fun fetchAndSaveTopics(order: TopicsOrder): Flow<Unit> = flow {
        emit(Unit)
    }

    override fun observeTopic(id: String): Flow<Topic> {
        return topicsFlow.map { topics -> topics.find { it.id == id }!! }
    }

    override fun getTopicPhotos(topicId: String): Flow<PagingData<Photo>> {
        return flowOf(PagingData.from(topicPhotosTestData))
    }

    /**
     * A test-only API to allow controlling the list of topics from tests.
     */
    fun sendTopics(topics: List<Topic>) {
        topicsFlow.tryEmit(topics)
    }
}
