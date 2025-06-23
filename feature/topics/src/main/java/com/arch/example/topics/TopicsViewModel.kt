package com.arch.example.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arch.example.common.result.AsyncResult
import com.arch.example.data.PreferencesRepository
import com.arch.example.domain.FetchTopicsUseCase
import com.arch.example.domain.ObserveTopicsUseCase
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.topics.model.TopicsUiEffect
import com.arch.example.topics.model.TopicsUiEvent
import com.arch.example.topics.model.TopicsUiState
import com.arch.example.ui.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TopicsViewModel @Inject constructor(
    observeTopicsUseCase: ObserveTopicsUseCase,
    private val fetchTopicsUseCase: FetchTopicsUseCase,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private var topicsFetchJob: Job? = null

    private val refreshing = MutableStateFlow(false)

    val uiState = combine(
        observeTopicsUseCase(),
        preferencesRepository.preferences().map { it.topicsOrder },
        refreshing,
    ) { topics, topicsOrder, refreshing ->
        TopicsUiState(
            topics = topics,
            topicsOrder = topicsOrder,
            loading = refreshing,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = TopicsUiState()
    )

    private val _uiEffect = Channel<TopicsUiEffect>(Channel.UNLIMITED)
    val uiEffect = _uiEffect.receiveAsFlow()

    init {
        viewModelScope.launch {
            preferencesRepository.preferences()
                .map { it.topicsOrder }
                .collect {
                    refreshTopics(order = it)
                }
        }
    }

    fun handleEvent(event: TopicsUiEvent) {
        when (event) {
            TopicsUiEvent.LoadTopics -> {
                refreshTopics()
            }

            is TopicsUiEvent.UpdateTopicOrder -> {
                updateTopicSorting(event.order)
            }
        }
    }

    private fun sendEffect(effect: TopicsUiEffect) {
        _uiEffect.trySend(effect)
    }

    private fun refreshTopics(order: TopicsOrder = uiState.value.topicsOrder) {
        topicsFetchJob?.cancel()
        topicsFetchJob = viewModelScope.launch {
            fetchTopicsUseCase(order).collect { result ->
                when (result) {
                    AsyncResult.Loading -> {
                        refreshing.update { true }
                    }

                    is AsyncResult.Success -> {
                        refreshing.update { false }
                    }

                    is AsyncResult.Error -> {
                        refreshing.update { false }
                        showSnackbarMessage(result.message)
                    }
                }
            }
        }
    }

    private fun updateTopicSorting(order: TopicsOrder) {
        viewModelScope.launch {
            preferencesRepository.updateTopicSorting(order)
        }
    }

    private fun showSnackbarMessage(message: String) {
        sendEffect(TopicsUiEffect.ShowSnackbarMessage(message))
    }
}
