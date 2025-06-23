package com.arch.example.topics.model

import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder

data class TopicsUiState(
    val topics: List<Topic> = emptyList(),
    val topicsOrder: TopicsOrder = TopicsOrder.POSITION,
    val loading: Boolean = true,
)
