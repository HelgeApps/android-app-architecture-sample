package com.arch.example.topicdetails


import androidx.lifecycle.SavedStateHandle
import com.arch.example.domain.GetTopicPhotosUseCase
import com.arch.example.domain.ObserveTopicUseCase
import com.arch.example.testing.data.topicsTestData
import com.arch.example.testing.repository.TestTopicRepository
import com.arch.example.testing.util.MainDispatcherRule
import com.arch.example.topicdetails.navigation.TopicDetails
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

/**
 * To learn more about how this test handles Flows created with stateIn, see
 * https://developer.android.com/kotlin/flow/test#statein
 */
class TopicDetailsViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private val topicsRepository = TestTopicRepository()

    private lateinit var viewModel: TopicDetailsViewModel

    @Before
    fun setup() {
        viewModel = TopicDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(TopicDetails.TOPIC_ID_ARG to topicsTestData[0].id)),
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