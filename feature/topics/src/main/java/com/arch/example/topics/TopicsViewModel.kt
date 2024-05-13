package com.arch.example.topics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arch.example.common.result.AsyncResult
import com.arch.example.data.PreferencesRepository
import com.arch.example.domain.FetchTopicsUseCase
import com.arch.example.domain.ObserveTopicsUseCase
import com.arch.example.entities.topic.Topic
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.ui.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TopicsUiState(
    val topics: List<Topic> = emptyList(),
    val topicsOrder: TopicsOrder = TopicsOrder.POSITION,
    val loading: Boolean = true,
    val userMessage: String? = null,
)

@HiltViewModel
class TopicsViewModel @Inject constructor(
    observeTopicsUseCase: ObserveTopicsUseCase,
    private val fetchTopicsUseCase: FetchTopicsUseCase,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {

    private var topicsFetchJob: Job? = null

    private val _userMessage: MutableStateFlow<String?> = MutableStateFlow(null)
    private val _refreshing = MutableStateFlow(false)

    val uiState = combine(
        observeTopicsUseCase(),
        preferencesRepository.preferences().map { it.topicsOrder },
        _refreshing,
        _userMessage
    ) { topics, topicsOrder, refreshing, userMessage ->
        TopicsUiState(
            topics = topics,
            topicsOrder = topicsOrder,
            loading = refreshing,
            userMessage = userMessage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileUiSubscribed,
        initialValue = TopicsUiState()
    )

    init {
        viewModelScope.launch {
            preferencesRepository.preferences()
                .map { it.topicsOrder }
                .collect {
                    refreshTopics(order = it)
                }
        }
    }

    fun refreshTopics(order: TopicsOrder = uiState.value.topicsOrder) {
        topicsFetchJob?.cancel()
        topicsFetchJob = viewModelScope.launch {
            fetchTopicsUseCase(order).collect { result ->
                when (result) {
                    is AsyncResult.Error -> {
                        _refreshing.value = false
                        showSnackbarMessage(result.message)
                    }

                    AsyncResult.Loading -> {
                        _refreshing.value = true
                    }

                    is AsyncResult.Success -> {
                        _refreshing.value = false
                    }
                }
            }
        }
    }

    fun updateTopicSorting(order: TopicsOrder) {
        viewModelScope.launch {
            preferencesRepository.updateTopicSorting(order)
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: String) {
        _userMessage.value = message
    }
}