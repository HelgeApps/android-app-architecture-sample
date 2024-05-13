package com.arch.example.data.testdoubles

import com.arch.example.database.dao.TopicDao
import com.arch.example.database.models.topic.DbTopicEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class TestTopicDao : TopicDao {

    private val entitiesStateFlow = MutableStateFlow(emptyList<DbTopicEntity>())

    override suspend fun insertAll(topics: List<DbTopicEntity>): List<Long> {
        // Keep old values over new values
        entitiesStateFlow.update { oldValues ->
            (oldValues + topics).distinctBy(DbTopicEntity::id)
        }
        return topics.indices.map { it.toLong() }.toList()
    }

    override fun getAll(): Flow<List<DbTopicEntity>> {
        return entitiesStateFlow
    }

    override fun getTopicById(id: String): Flow<DbTopicEntity> {
        return entitiesStateFlow.map { it.first { topic -> topic.id in id } }
    }

    override suspend fun deleteAll(): Int {
        val size = entitiesStateFlow.value.size
        entitiesStateFlow.update { emptyList() }
        return size
    }
}
