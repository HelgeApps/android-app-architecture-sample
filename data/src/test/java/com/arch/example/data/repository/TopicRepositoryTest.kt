package com.arch.example.data.repository

import com.arch.example.data.TopicRepository
import com.arch.example.data.implementation.TopicRepositoryImpl
import com.arch.example.data.testdoubles.TestTopicDao
import com.arch.example.data.testdoubles.TestTopicNetworkDataSource
import com.arch.example.database.dao.TopicDao
import com.arch.example.database.manager.TopicDbManager
import com.arch.example.database.manager.implementation.TopicDbManagerImpl
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.network.adapters.DateTimeTypeAdapter
import com.arch.example.network.manager.TopicNetworkDataSource
import com.arch.example.network.models.topic.asExternalModel
import com.squareup.moshi.Moshi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TopicRepositoryTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val testScope = TestScope(testDispatcher)

    private lateinit var subject: TopicRepository

    private lateinit var topicDao: TopicDao

    private lateinit var database: TopicDbManager

    private lateinit var network: TopicNetworkDataSource

    @Before
    fun setup() {
        topicDao = TestTopicDao()
        database = TopicDbManagerImpl(
            topicDao = topicDao
        )
        network = TestTopicNetworkDataSource(
            ioDispatcher = testDispatcher,
            moshi = Moshi.Builder()
                .add(DateTimeTypeAdapter())
                .build()
        )

        subject = TopicRepositoryImpl(
            topicDbManager = database,
            topicNetworkDataSource = network,
        )
    }

    @Test
    fun topicRepository_topics_stream_is_backed_by_topics_database() =
        testScope.runTest {
            // fetch topics from network and save to DB
            subject.fetchAndSaveTopics(TopicsOrder.LATEST).collect()

            assertEquals(
                database.getAllTopics()
                    .first(),
                subject.observeTopics()
                    .first(),
            )
        }

    @Test
    fun topicsRepository_topics_saved_in_db_correctly_after_fetched_from_network() =
        testScope.runTest {
            subject.fetchAndSaveTopics(TopicsOrder.LATEST).collect()

            val networkTopics = network.getTopics(
                page = 1,
                perPage = 10,
                orderBy = TopicsOrder.LATEST
            ).map { it.asExternalModel() }

            val dbTopics = database.getAllTopics()
                .first()

            assertEquals(
                networkTopics,
                dbTopics,
            )
        }
}
