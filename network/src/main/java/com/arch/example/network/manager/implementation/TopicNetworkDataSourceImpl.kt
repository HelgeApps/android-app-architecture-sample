package com.arch.example.network.manager.implementation

import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.api.TopicApi
import com.arch.example.network.manager.TopicNetworkDataSource
import com.arch.example.network.models.photo.NetworkPhoto
import com.arch.example.network.models.topic.NetworkTopic
import com.arch.example.network.utils.NetworkErrorConverterHelper

class TopicNetworkDataSourceImpl(
    private val topicApi: TopicApi,
    private val networkErrorConverterHelper: NetworkErrorConverterHelper
) : TopicNetworkDataSource {

    override suspend fun getTopics(
        page: Int,
        perPage: Int,
        orderBy: TopicsOrder
    ): List<NetworkTopic> = try {
        topicApi.getTopics(page, perPage, orderBy)
    } catch (e: Throwable) {
        throw networkErrorConverterHelper.parseError(e)
    }

    override suspend fun getTopicPhotos(
        topicId: String,
        page: Int,
        perPage: Int
    ): List<NetworkPhoto> =
        try {
            topicApi.getTopicPhotos(topicId, page, perPage)
        } catch (e: Throwable) {
            throw networkErrorConverterHelper.parseError(e)
        }
}
