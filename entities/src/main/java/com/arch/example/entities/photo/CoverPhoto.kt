package com.arch.example.entities.photo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CoverPhoto(
    val photoUrls: PhotoUrls?
) : Parcelable