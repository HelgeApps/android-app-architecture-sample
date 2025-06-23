package com.arch.example.topics.model

import com.arch.example.entities.topic.TopicsOrder

sealed class TopicsUiEvent {
    data class UpdateTopicOrder(val order: TopicsOrder) : TopicsUiEvent()
    data object LoadTopics : TopicsUiEvent()
}
