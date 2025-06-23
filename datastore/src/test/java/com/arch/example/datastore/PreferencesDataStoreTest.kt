package com.arch.example.datastore

import com.arch.example.datastore.implementation.PreferencesDataStoreImpl
import com.arch.example.datastore.test.InMemoryDataStore
import com.arch.example.entities.Preferences
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

class PreferencesDataStoreTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: PreferencesDataStore

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = PreferencesDataStoreImpl(InMemoryDataStore(Preferences()))
    }

    @Test
    fun shouldTopicsOrderByPositionByDefault() = testScope.runTest {
        assertEquals(subject.preferences().first().topicsOrder, TopicsOrder.POSITION)
    }

    @Test
    fun shouldTopicsOrderByLatestWhenSet() = testScope.runTest {
        subject.updateTopicSorting(TopicsOrder.LATEST)
        assertEquals(subject.preferences().first().topicsOrder, TopicsOrder.LATEST)
    }
}
