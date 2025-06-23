package com.arch.example.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.arch.example.database.dao.TopicDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TopicDaoTest {

    private lateinit var topicDao: TopicDao
    private lateinit var db: AppDataBase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDataBase::class.java,
        ).build()
        topicDao = db.getTopicsDao()
    }

    @After
    fun closeDb() = db.close()

    @Test
    fun topicDao_items_empty_by_default() = runTest {
        val items = topicDao.getAll().first()

        assertEquals(
            items,
            emptyList()
        )
    }

    // TODO: test inserting, deleting, getting by id
}
