package com.arch.example.data.testdoubles

import JvmUnitTestAssetManager
import com.arch.example.common.AppDispatchers
import com.arch.example.common.Dispatcher
import com.arch.example.data.test.TestAssetManager
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.manager.TopicNetworkDataSource
import com.arch.example.network.models.photo.NetworkPhoto
import com.arch.example.network.models.topic.NetworkTopic
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source

class TestTopicNetworkDataSource(
    @Dispatcher(AppDispatchers.Default) private val ioDispatcher: CoroutineDispatcher,
    private val moshi: Moshi,
    private val assets: TestAssetManager = JvmUnitTestAssetManager,
) : TopicNetworkDataSource {

    override suspend fun getTopics(
        page: Int,
        perPage: Int,
        orderBy: TopicsOrder
    ): List<NetworkTopic> = withContext(ioDispatcher) {
        assets.open(TOPICS_RESPONSE_ASSET).use {
            val type = Types.newParameterizedType(
                List::class.java,
                NetworkTopic::class.java
            )
            moshi.adapter<List<NetworkTopic>>(type).fromJson(it.source().buffer())!!
        }
    }

    override suspend fun getTopicPhotos(
        topicId: String,
        page: Int,
        perPage: Int
    ): List<NetworkPhoto> = withContext(ioDispatcher) {
        assets.open(PHOTOS_RESPONSE_ASSET).use {
            val type = Types.newParameterizedType(
                List::class.java,
                NetworkPhoto::class.java
            )
            moshi.adapter<List<NetworkPhoto>>(type).fromJson(it.source().buffer())!!
        }
    }

    companion object {
        private const val TOPICS_RESPONSE_ASSET = "topics_response.json"
        private const val PHOTOS_RESPONSE_ASSET = "photos_response.json"
    }
}
