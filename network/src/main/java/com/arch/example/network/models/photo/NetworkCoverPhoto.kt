package com.arch.example.network.models.photo

import com.arch.example.entities.photo.CoverPhoto
import com.arch.example.network.models.photo.NetworkPhotoUrls
import com.arch.example.network.models.photo.asExternalModel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkCoverPhoto(
    @Json(name = "urls")
    val photoUrls: NetworkPhotoUrls?
)

fun NetworkCoverPhoto.asExternalModel() = CoverPhoto(
    photoUrls = photoUrls?.asExternalModel()
)