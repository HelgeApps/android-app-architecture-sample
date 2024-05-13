package com.arch.example.testing.repository

import com.arch.example.data.PreferencesRepository
import com.arch.example.entities.Preferences
import com.arch.example.entities.topic.TopicsOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TestPreferencesRepository : PreferencesRepository {

    private val preferencesFlow = MutableStateFlow((Preferences()))

    override fun preferences(): Flow<Preferences> = preferencesFlow

    override suspend fun updateTopicSorting(order: TopicsOrder) {
        preferencesFlow.tryEmit(preferencesFlow.value.copy(topicsOrder = order))
    }
}
