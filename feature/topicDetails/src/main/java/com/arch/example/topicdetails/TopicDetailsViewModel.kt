package com.arch.example.topicdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arch.example.domain.GetTopicPhotosUseCase
import com.arch.example.domain.ObserveTopicUseCase
import com.arch.example.entities.topic.Topic
import com.arch.example.topicdetails.navigation.TopicDetails
import com.arch.example.ui.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TopicDetailsViewModel @Inject constructor(
    observeTopicUseCase: ObserveTopicUseCase,
    getTopicPhotosUseCase: GetTopicPhotosUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    val topicId: String = savedStateHandle[TopicDetails.TOPIC_ID_ARG]!!

    val topicUiState = observeTopicUseCase(topicId)
        .map { TopicUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = TopicUiState.Loading
        )

    val photosResponse = getTopicPhotosUseCase(topicId)
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = PagingData.empty()
        )
}

sealed interface TopicUiState {
    data class Success(val topic: Topic) : TopicUiState
    data object Loading : TopicUiState
}