package com.arch.example.topicdetails

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.arch.example.entities.photo.Photo
import com.arch.example.testing.data.topicsTestData
import com.arch.example.topicdetails.model.TopicUiState
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * UI test for checking the correct behaviour of the Topic Detail screen;
 * Verifies that, when a specific UiState is set, the corresponding
 * composables and details are shown
 */
class TopicDetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.activity
    }

    @Test
    fun circularProgressIndicator_whenScreenIsLoading_exists() {
        composeTestRule.setContent {
            TopicDetailsContent(
                modifier = Modifier.padding(),
                uiState = TopicUiState.Loading,
                photosResponse = flowOf(PagingData.from(emptyList<Photo>()))
                    .collectAsLazyPagingItems(),
                navigateUp = {},
                navigateToPhotoDetails = {},
            )
        }

        val loadingContentDescription = composeTestRule.activity.resources.getString(
            R.string.content_description_loading
        )

        composeTestRule
            .onNodeWithContentDescription(loadingContentDescription)
            .assertExists()
    }

    @Test
    fun topicDetailsTitle_whenTopicIsSuccess_isShown() {
        val testTopic = topicsTestData.first()
        composeTestRule.setContent {
            TopicDetailsContent(
                modifier = Modifier.padding(),
                uiState = TopicUiState.Success(testTopic),
                photosResponse = flowOf(PagingData.from(emptyList<Photo>()))
                    .collectAsLazyPagingItems(),
                navigateUp = {},
                navigateToPhotoDetails = {},
            )
        }

        // Title is shown (on the toolbar and header)
        composeTestRule
            .onAllNodesWithText(testTopic.title)
            .assertCountEquals(2)

        // Description is shown
        testTopic.description?.let {
            composeTestRule
                .onNodeWithText(it)
                .assertExists()
        }
    }
}
