package com.arch.example.data.implementation

import com.arch.example.data.PreferencesRepository
import com.arch.example.datastore.PreferencesDataStore
import com.arch.example.entities.Preferences
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : PreferencesRepository {

    override fun preferences(): Flow<Preferences> = preferencesDataStore.preferences()

    override suspend fun updateTopicSorting(order: TopicsOrder) {
        preferencesDataStore.updateTopicSorting(order)
    }
}
