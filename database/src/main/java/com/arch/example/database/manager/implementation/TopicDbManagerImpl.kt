package com.arch.example.database.manager.implementation

import com.arch.example.database.dao.TopicDao
import com.arch.example.database.manager.TopicDbManager
import com.arch.example.database.models.topic.DbTopicEntity
import com.arch.example.database.models.topic.asEntity
import com.arch.example.database.models.topic.asExternalModel
import com.arch.example.entities.topic.Topic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TopicDbManagerImpl(private val topicDao: TopicDao) : TopicDbManager {

    override fun getAllTopics(): Flow<List<Topic>> {
        return topicDao.getAll().map { dbTopics ->
            dbTopics.map(DbTopicEntity::asExternalModel)
        }
    }

    override fun getTopicById(id: String): Flow<Topic> {
        return topicDao.getTopicById(id).map(DbTopicEntity::asExternalModel)
    }

    override suspend fun deleteAllAndAddNewTopics(topics: List<Topic>) {
        topicDao.deleteAllAndAddNewTopics(topics.map(Topic::asEntity))
    }
}