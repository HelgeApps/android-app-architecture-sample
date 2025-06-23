package com.arch.example.topicdetails


import androidx.lifecycle.SavedStateHandle
import androidx.navigation.testing.invoke
import com.arch.example.domain.GetTopicPhotosUseCase
import com.arch.example.domain.ObserveTopicUseCase
import com.arch.example.testing.data.topicsTestData
import com.arch.example.testing.repository.TestTopicRepository
import com.arch.example.testing.util.MainDispatcherRule
import com.arch.example.topicdetails.model.TopicUiState
import com.arch.example.topicdetails.navigation.TopicDetailsRoute
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 *
 * These tests use Robolectric because the subject under test (the ViewModel) uses
 * `SavedStateHandle.toRoute` which has a dependency on `android.os.Bundle`.
 *
 * TODO: Remove Robolectric if/when AndroidX Navigation API is updated to remove Android dependency.
 *  See https://issuetracker.google.com/340966212.
 */
@RunWith(RobolectricTestRunner::class)
class TopicDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val topicsRepository = TestTopicRepository()

    private lateinit var viewModel: TopicDetailsViewModel

    @Before
    fun setup() {
        viewModel = TopicDetailsViewModel(
            savedStateHandle = SavedStateHandle(
                route = TopicDetailsRoute(topicId = topicsTestData[0].id)
            ),
            observeTopicUseCase = ObserveTopicUseCase(topicsRepository),
            getTopicPhotosUseCase = GetTopicPhotosUseCase(topicsRepository)
        )
    }

    @Test
    fun topicId_matchesTopicIdFromSavedStateHandle() =
        assertEquals(topicsTestData[0].id, viewModel.topicId)

    @Test
    fun uiStateTopic_whenSuccess_matchesTopicFromRepository() = runTest {
        val collectJob = launch(UnconfinedTestDispatcher()) {
            viewModel.topicUiState.collect()
        }

        topicsRepository.sendTopics(topicsTestData)

        val item = viewModel.topicUiState.value
        assertIs<TopicUiState.Success>(item)

        val topicFromRepository = topicsRepository.observeTopic(
            topicsTestData[0].id,
        ).first()

        assertEquals(topicFromRepository, item.topic)

        collectJob.cancel()
    }

    @Test
    fun uiStateTopic_whenInitialized_thenShowLoading() = runTest {
        assertEquals(TopicUiState.Loading, viewModel.topicUiState.value)
    }
}
