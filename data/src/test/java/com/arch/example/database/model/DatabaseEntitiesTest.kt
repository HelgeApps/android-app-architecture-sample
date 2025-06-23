package com.arch.example.database.model

import com.arch.example.database.models.photo.DbCoverPhoto
import com.arch.example.database.models.photo.DbPhotoUrls
import com.arch.example.database.models.photo.asEntity
import com.arch.example.database.models.photo.asExternalModel
import com.arch.example.database.models.topic.DbTopicEntity
import com.arch.example.database.models.topic.asEntity
import com.arch.example.database.models.topic.asExternalModel
import com.arch.example.entities.photo.CoverPhoto
import com.arch.example.entities.photo.PhotoUrls
import com.arch.example.entities.topic.Topic
import org.junit.Test
import java.time.Instant
import kotlin.test.assertEquals

class DatabaseEntitiesTest {

    // DB Entity to External Model:

    @Test
    fun entity_topic_can_be_mapped_to_topic_external_model() {
        val entityModel = DbTopicEntity(
            id = "0",
            title = "Title",
            description = "Desc",
            publishedAt = Instant.ofEpochMilli(1),
            coverPhoto = null
        )
        val externalModel = entityModel.asExternalModel()

        assertEquals("0", externalModel.id)
        assertEquals("Title", externalModel.title)
        assertEquals("Desc", externalModel.description)
        assertEquals(Instant.ofEpochMilli(1), externalModel.publishedAt)
        assertEquals(null, externalModel.coverPhoto)
    }

    @Test
    fun entity_cover_photo_can_be_mapped_to_cover_photo_external_model() {
        val entityModel = DbCoverPhoto(
            photoUrls = DbPhotoUrls(
                raw = null,
                full = null,
                regular = "https://example.com/1.jpg",
                small = null,
                thumb = null,
            )
        )
        val externalModel = entityModel.asExternalModel()

        assertEquals(null, externalModel.photoUrls?.raw)
        assertEquals(null, externalModel.photoUrls?.full)
        assertEquals("https://example.com/1.jpg", externalModel.photoUrls?.regular)
        assertEquals(null, externalModel.photoUrls?.small)
        assertEquals(null, externalModel.photoUrls?.thumb)
    }

    // External Model to DB Entity:

    @Test
    fun external_topic_can_be_mapped_to_topic_entity() {
        val entityModel = Topic(
            id = "0",
            title = "Title",
            description = "Desc",
            publishedAt = Instant.ofEpochMilli(1),
            coverPhoto = null
        )
        val externalModel = entityModel.asEntity()

        assertEquals("0", externalModel.id)
        assertEquals("Title", externalModel.title)
        assertEquals("Desc", externalModel.description)
        assertEquals(Instant.ofEpochMilli(1), externalModel.publishedAt)
        assertEquals(null, externalModel.coverPhoto)
    }

    @Test
    fun external_cover_photo_can_be_mapped_to_cover_photo_entity() {
        val entityModel = CoverPhoto(
            photoUrls = PhotoUrls(
                raw = null,
                full = null,
                regular = "https://example.com/1.jpg",
                small = null,
                thumb = null,
            )
        )
        val externalModel = entityModel.asEntity()

        assertEquals(null, externalModel.photoUrls?.raw)
        assertEquals(null, externalModel.photoUrls?.full)
        assertEquals("https://example.com/1.jpg", externalModel.photoUrls?.regular)
        assertEquals(null, externalModel.photoUrls?.small)
        assertEquals(null, externalModel.photoUrls?.thumb)
    }
}
