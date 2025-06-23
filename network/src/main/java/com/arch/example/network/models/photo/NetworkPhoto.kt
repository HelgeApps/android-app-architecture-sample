package com.arch.example.network.models.photo

import com.arch.example.entities.photo.Photo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class NetworkPhoto(
    @Json(name = "id")
    val id: String,

    @Json(name = "description")
    val description: String?,

    @Json(name = "alt_description")
    val altDescription: String?,

    @Json(name = "created_at")
    val createdAt: Instant,

    @Json(name = "color")
    val color: String?,

    @Json(name = "urls")
    val photoUrls: NetworkPhotoUrls,

    @Json(name = "width")
    val width: Int?,

    @Json(name = "height")
    val height: Int?
)

fun NetworkPhoto.asExternalModel() = Photo(
    id = this.id,
    description = this.description,
    altDescription = this.altDescription,
    createdAt = this.createdAt,
    color = this.color,
    photoUrls = this.photoUrls.asExternalModel(),
    width = this.width,
    height = this.height
)