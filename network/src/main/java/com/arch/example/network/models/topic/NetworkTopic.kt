package com.arch.example.network.models.topic

import com.arch.example.entities.topic.Topic
import com.arch.example.network.models.photo.NetworkCoverPhoto
import com.arch.example.network.models.photo.asExternalModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class NetworkTopic(
    @Json(name = "id")
    val id: String,

    @Json(name = "title")
    val title: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "published_at")
    val publishedAt: Instant,

    @Json(name = "cover_photo")
    val coverPhoto: NetworkCoverPhoto?
)

fun NetworkTopic.asExternalModel() = Topic(
    id = this.id,
    title = this.title,
    description = this.description,
    publishedAt = this.publishedAt,
    coverPhoto = this.coverPhoto?.asExternalModel()
)