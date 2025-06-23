package com.arch.example.topics

import com.arch.example.common.result.AsyncResult
import com.arch.example.data.PreferencesRepository
import com.arch.example.domain.FetchTopicsUseCase
import com.arch.example.domain.ObserveTopicsUseCase
import com.arch.example.entities.Preferences
import com.arch.example.testing.data.topicsTestData
import com.arch.example.testing.util.MainDispatcherRule
import com.arch.example.topics.model.TopicsUiState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

/**
 * A test double that behaves how you program it to behave but doesn't have expectations about its interactions.
 * Usually created with a mocking framework (Mockito in this example).
 * Fakes are preferred over stubs for simplicity.
 * https://developer.android.com/training/testing/fundamentals/test-doubles
 */
class TopicsViewModelMockDependenciesTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val preferences = Preferences()

    private val observeTopicsUseCase: ObserveTopicsUseCase = mock {
        on { invoke() } doReturn flowOf(topicsTestData)
    }

    private val fetchTopicsUseCase: FetchTopicsUseCase = mock {
        on { invoke(preferences.topicsOrder) } doReturn flowOf(AsyncResult.Success(Unit))
    }

    private val preferencesRepository: PreferencesRepository = mock {
        on { preferences() } doReturn flowOf(preferences)
    }

    private lateinit var viewModel: TopicsViewModel

    @Before
    fun setup() {
        viewModel = TopicsViewModel(
            observeTopicsUseCase = observeTopicsUseCase,
            fetchTopicsUseCase = fetchTopicsUseCase,
            preferencesRepository = preferencesRepository
        )
    }

    @Test
    fun uiStateCheckInitialState() = runTest {
        assertEquals(TopicsUiState(), viewModel.uiState.value)
    }

    @Test
    fun stateHasTopicItemsAndNotLoadingWhenUpdated() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) { viewModel.uiState.collect() }

        assertEquals(
            viewModel.uiState.value,
            TopicsUiState(
                topics = topicsTestData,
                loading = false
            )
        )

        collectJob.cancel()
    }
}
