package com.arch.example.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.arch.example.entities.photo.Photo
import com.arch.example.entities.photo.PhotoUrls
import com.arch.example.designsystem.theme.AppTheme
import com.arch.example.ui.utils.Formats
import java.time.Instant
import java.util.*


@Composable
fun PhotoListItem(
    photo: Photo,
    modifier: Modifier = Modifier,
    navigateToPhotoDetails: (id: String) -> Unit = {},
) {
    val ratio = if (photo.width != null && photo.height != null) {
        // https://discuss.kotlinlang.org/t/what-is-the-reason-behind-smart-cast-being-impossible-to-perform-when-referenced-class-is-in-another-module/2201
        photo.width!!.toFloat() / photo.height!!.toFloat()
    } else 0f
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(8.dp)
            .aspectRatio(ratio)
            .clickable { navigateToPhotoDetails(photo.id) }
    ) {
        Box {
            val imageUrl = photo.photoUrls.let {
                it.thumb ?: it.small ?: it.raw ?: it.regular ?: it.full
            }
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Photo image",
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        photo.color?.let { Color(android.graphics.Color.parseColor(it)) }
                            ?: Color.DarkGray
                    )
            )
            (photo.description ?: photo.altDescription)?.let {
                Text(
                    text = Formats.removeHtmlTags(it),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(6.dp)
                        .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .align(Alignment.BottomStart)
                        .basicMarquee()
                )
            }
        }
    }
}


@Preview("Regular colors")
@Preview("Dark colors", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewPhotoItem() {
    AppTheme {
        Surface {
            PhotoListItem(
                Photo(
                    id = UUID.randomUUID().toString(),
                    description = "Photo description",
                    altDescription = null,
                    createdAt = Instant.now(),
                    color = null,
                    width = 300,
                    height = 200,
                    photoUrls = PhotoUrls(null, null, null, null, null)
                )
            ) {}
        }
    }
}