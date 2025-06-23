package com.arch.example.topicdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arch.example.domain.GetTopicPhotosUseCase
import com.arch.example.domain.ObserveTopicUseCase
import com.arch.example.topicdetails.model.TopicUiState
import com.arch.example.topicdetails.navigation.TopicDetailsRoute
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
    val topicId: String = savedStateHandle.toRoute<TopicDetailsRoute>().topicId

    val topicUiState = observeTopicUseCase(topicId)
        .map { TopicUiState.Success(it) }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = TopicUiState.Loading
        )

    val photosPagingFlow = getTopicPhotosUseCase(topicId)
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = PagingData.empty()
        )
}
