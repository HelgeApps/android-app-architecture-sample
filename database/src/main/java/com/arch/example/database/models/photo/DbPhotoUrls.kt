package com.arch.example.database.models.photo

import com.arch.example.entities.photo.PhotoUrls

data class DbPhotoUrls(
    val raw: String?,
    val full: String?,
    val regular: String?,
    val small: String?,
    val thumb: String?
)

fun DbPhotoUrls.asExternalModel() = PhotoUrls(
    raw = this.raw,
    full = this.full,
    regular = this.regular,
    small = this.small,
    thumb = this.thumb
)

fun PhotoUrls.asEntity() = DbPhotoUrls(
    raw = this.raw,
    full = this.full,
    regular = this.regular,
    small = this.small,
    thumb = this.thumb
)