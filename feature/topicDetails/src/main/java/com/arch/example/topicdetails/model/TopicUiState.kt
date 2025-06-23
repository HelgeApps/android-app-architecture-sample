package com.arch.example.topicdetails.model

import com.arch.example.entities.topic.Topic

sealed interface TopicUiState {
    data class Success(val topic: Topic) : TopicUiState
    data object Loading : TopicUiState
}
