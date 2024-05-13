package com.arch.example.network.models.photo

import com.arch.example.entities.photo.PhotoUrls
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkPhotoUrls(
    @Json(name = "raw") val raw: String? = null,
    @Json(name = "full") val full: String? = null,
    @Json(name = "regular") val regular: String? = null,
    @Json(name = "small") val small: String? = null,
    @Json(name = "thumb") val thumb: String? = null
)

fun NetworkPhotoUrls.asExternalModel() = PhotoUrls(
    raw = this.raw,
    full = this.full,
    regular = this.regular,
    small = this.small,
    thumb = this.thumb
)