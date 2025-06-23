package com.arch.example.data

import com.arch.example.entities.Preferences
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun preferences(): Flow<Preferences>

    suspend fun updateTopicSorting(order: TopicsOrder)
}
