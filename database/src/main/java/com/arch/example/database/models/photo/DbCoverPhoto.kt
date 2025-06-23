package com.arch.example.database.models.photo

import androidx.room.Embedded
import com.arch.example.entities.photo.CoverPhoto

data class DbCoverPhoto(
    @Embedded(prefix = "photo_urls")
    val photoUrls: DbPhotoUrls?
)

fun DbCoverPhoto.asExternalModel() = CoverPhoto(
    photoUrls = photoUrls?.asExternalModel()
)

fun CoverPhoto.asEntity() = DbCoverPhoto(
    photoUrls = photoUrls?.asEntity()
)
