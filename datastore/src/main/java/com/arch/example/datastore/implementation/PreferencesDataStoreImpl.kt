package com.arch.example.datastore.implementation

import androidx.datastore.core.DataStore
import com.arch.example.datastore.PreferencesDataStore
import com.arch.example.entities.Preferences
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class PreferencesDataStoreImpl(
    private val dataStore: DataStore<Preferences>
) : PreferencesDataStore {

    override fun preferences(): Flow<Preferences> = dataStore.data

    override suspend fun updateTopicSorting(order: TopicsOrder) {
        dataStore.updateData { it.copy(topicsOrder = order) }
    }
}