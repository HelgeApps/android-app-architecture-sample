package com.arch.example.data.model

import com.arch.example.network.models.photo.NetworkCoverPhoto
import com.arch.example.network.models.photo.NetworkPhotoUrls
import com.arch.example.network.models.photo.asExternalModel
import com.arch.example.network.models.topic.NetworkTopic
import com.arch.example.network.models.topic.asExternalModel
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals

class NetworkModelsTest {

    @Test
    fun network_topic_can_be_mapped_to_topic_external_model() {
        val networkModel = NetworkTopic(
            id = "0",
            title = "Title",
            description = "Desc",
            publishedAt = Instant.ofEpochMilli(1),
            coverPhoto = null
        )
        val externalModel = networkModel.asExternalModel()

        assertEquals("0", externalModel.id)
        assertEquals("Title", externalModel.title)
        assertEquals("Desc", externalModel.description)
        assertEquals(Instant.ofEpochMilli(1), externalModel.publishedAt)
        assertEquals(null, externalModel.coverPhoto)
    }

    @Test
    fun network_cover_photo_can_be_mapped_to_cover_photo_external_model() {
        val networkModel = NetworkCoverPhoto(
            photoUrls = NetworkPhotoUrls(
                regular = "https://example.com/1.jpg"
            )
        )
        val externalModel = networkModel.asExternalModel()

        assertEquals(null, externalModel.photoUrls?.raw)
        assertEquals(null, externalModel.photoUrls?.full)
        assertEquals("https://example.com/1.jpg", externalModel.photoUrls?.regular)
        assertEquals(null, externalModel.photoUrls?.small)
        assertEquals(null, externalModel.photoUrls?.thumb)
    }
}
