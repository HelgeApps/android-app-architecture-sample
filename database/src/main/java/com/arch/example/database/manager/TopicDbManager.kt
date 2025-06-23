package com.arch.example.database.manager

import com.arch.example.entities.topic.Topic
import kotlinx.coroutines.flow.Flow

interface TopicDbManager {
    fun getAllTopics(): Flow<List<Topic>>

    fun getTopicById(id: String): Flow<Topic>

    suspend fun deleteAllAndAddNewTopics(topics: List<Topic>)
}