package com.arch.example.datastore

import com.arch.example.entities.Preferences
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.Flow

interface PreferencesDataStore {

    fun preferences(): Flow<Preferences>

    suspend fun updateTopicSorting(order: TopicsOrder)
}