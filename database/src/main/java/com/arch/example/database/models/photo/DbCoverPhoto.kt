package com.arch.example.database.models.photo

import androidx.room.Embedded
import com.arch.example.entities.photo.CoverPhoto

data class DbCoverPhoto(
    @Embedded(prefix = COLUMN_PHOTO_URLS)
    val photoUrls: DbPhotoUrls?
) {
    companion object {
        const val COLUMN_PHOTO_URLS = "photo_urls"
    }
}

fun DbCoverPhoto.asExternalModel() = CoverPhoto(
    photoUrls = photoUrls?.asExternalModel()
)

fun CoverPhoto.asEntity() = DbCoverPhoto(
    photoUrls = photoUrls?.asEntity()
)