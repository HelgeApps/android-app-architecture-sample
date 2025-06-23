package com.arch.example.topics


import com.arch.example.domain.FetchTopicsUseCase
import com.arch.example.domain.ObserveTopicsUseCase
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.testing.data.topicsTestData
import com.arch.example.testing.repository.TestPreferencesRepository
import com.arch.example.testing.repository.TestTopicRepository
import com.arch.example.testing.util.MainDispatcherRule
import com.arch.example.topics.model.TopicsUiEvent
import com.arch.example.topics.model.TopicsUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class TopicsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val topicsRepository = TestTopicRepository()

    private val preferencesRepository = TestPreferencesRepository()

    private lateinit var viewModel: TopicsViewModel

    @Before
    fun setup() {
        viewModel = TopicsViewModel(
            observeTopicsUseCase = ObserveTopicsUseCase(topicsRepository),
            fetchTopicsUseCase = FetchTopicsUseCase(topicsRepository),
            preferencesRepository = preferencesRepository
        )
    }

    @Test
    fun uiStateCheckInitialState() = runTest {
        assertEquals(TopicsUiState(), viewModel.uiState.value)
    }

    @Test
    fun stateHasTopicItemsAndNotLoadingWhenUpdated() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        topicsRepository.sendTopics(topicsTestData)

        assertEquals(
            viewModel.uiState.value,
            TopicsUiState(
                topics = topicsTestData,
                loading = false
            )
        )

        collectJob.cancel()
    }

    @Test
    fun stateHasTopicOrderWhichWasSet() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.uiState.collect()
        }

        topicsRepository.sendTopics(emptyList())

        TopicsOrder.entries.forEach { topicsOrder ->
            viewModel.handleEvent(TopicsUiEvent.UpdateTopicOrder(topicsOrder))

            assertEquals(
                viewModel.uiState.value,
                TopicsUiState(
                    topicsOrder = topicsOrder,
                    loading = false
                )
            )
        }

        collectJob.cancel()
    }
}
