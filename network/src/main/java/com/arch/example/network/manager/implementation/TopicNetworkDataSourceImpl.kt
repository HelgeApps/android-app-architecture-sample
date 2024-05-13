package com.arch.example.network.manager.implementation

import com.arch.example.entities.photo.Photo
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.api.TopicApi
import com.arch.example.network.manager.TopicNetworkDataSource
import com.arch.example.network.models.photo.asExternalModel
import com.arch.example.network.models.topic.asExternalModel
import com.arch.example.network.utils.NetworkErrorConverterHelper

class TopicNetworkDataSourceImpl(
    private val topicApi: TopicApi,
    private val networkErrorConverterHelper: NetworkErrorConverterHelper
) : TopicNetworkDataSource {

    override suspend fun getTopics(
        page: Int,
        perPage: Int,
        orderBy: TopicsOrder
    ): List<Topic> = try {
        val data = topicApi.getTopics(page, perPage, orderBy)
        data.map { it.asExternalModel() }
    } catch (e: Throwable) {
        throw networkErrorConverterHelper.parseError(e)
    }

    override suspend fun getTopicPhotos(topicId: String, page: Int, perPage: Int): List<Photo> =
        try {
            val data = topicApi.getTopicPhotos(topicId, page, perPage)
            data.map { it.asExternalModel() }
        } catch (e: Throwable) {
            throw networkErrorConverterHelper.parseError(e)
        }
}