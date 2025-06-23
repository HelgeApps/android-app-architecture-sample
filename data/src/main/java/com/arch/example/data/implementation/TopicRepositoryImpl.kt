package com.arch.example.data.implementation

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.arch.example.data.TopicRepository
import com.arch.example.data.implementation.paging.TopicPhotosPagingSource
import com.arch.example.database.manager.TopicDbManager
import com.arch.example.entities.photo.Photo
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.manager.TopicNetworkDataSource
import com.arch.example.network.models.topic.asExternalModel
import com.arch.example.network.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TopicRepositoryImpl @Inject constructor(
    private val topicNetworkDataSource: TopicNetworkDataSource,
    private val topicDbManager: TopicDbManager
) : TopicRepository {

    override fun observeTopics(): Flow<List<Topic>> {
        return topicDbManager.getAllTopics()
    }

    override fun fetchAndSaveTopics(order: TopicsOrder): Flow<Unit> = flow {
        val topics = topicNetworkDataSource.getTopics(
            page = 1,
            perPage = 10,
            orderBy = order
        ).map { it.asExternalModel() }
        topicDbManager.deleteAllAndAddNewTopics(topics)
        emit(Unit)
    }

    override fun observeTopic(id: String): Flow<Topic> {
        return topicDbManager.getTopicById(id)
    }

    override fun getTopicPhotos(topicId: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = PAGE_SIZE,
                initialLoadSize = PAGE_SIZE * 2
            ),
            pagingSourceFactory = {
                TopicPhotosPagingSource(
                    topicNetworkDataSource = topicNetworkDataSource,
                    topicId = topicId
                )
            }
        ).flow
    }
}
