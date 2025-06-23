package com.arch.example.topics

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.entities.photo.smallestAvailable
import com.arch.example.entities.topic.Topic
import com.arch.example.ui.utils.FormatUtils
import java.time.Instant
import java.util.UUID


@Composable
fun TopicListItem(
    topic: Topic,
    navigateToTopicDetails: (id: String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToTopicDetails(topic.id) }
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(topic.coverPhoto?.photoUrls?.smallestAvailable)
                .crossfade(true)
                .build(),
            contentDescription = "Topic Logo",
            modifier = Modifier
                .size(72.dp)
                .background(Color.Black)
        )
        Spacer(Modifier.width(8.dp))
        Column {
            Text(
                text = topic.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
            )
            topic.description?.let { desc ->
                Text(
                    text = remember(desc) {
                        FormatUtils.removeHtmlTags(desc)
                    },
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = FormatUtils.dateFormat(topic.publishedAt),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Preview("Light theme")
@Preview("Dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTopicItem() {
    AppTheme {
        Surface {
            TopicListItem(
                Topic(
                    id = UUID.randomUUID().toString(),
                    title = "Test topic",
                    description = "Some description",
                    publishedAt = Instant.now(),
                    coverPhoto = null
                )
            ) {}
        }
    }
}
