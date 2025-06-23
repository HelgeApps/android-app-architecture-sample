package com.arch.example.database.models.photo

import androidx.room.ColumnInfo
import com.arch.example.entities.photo.PhotoUrls

data class DbPhotoUrls(
    @ColumnInfo(name = "raw")
    val raw: String?,

    @ColumnInfo(name = "full")
    val full: String?,

    @ColumnInfo(name = "regular")
    val regular: String?,

    @ColumnInfo(name = "small")
    val small: String?,

    @ColumnInfo(name = "thumb")
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
