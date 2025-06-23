package com.arch.example.entities.photo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoUrls(
    val raw: String? = null,
    val full: String? = null,
    val regular: String? = null,
    val small: String? = null,
    val thumb: String? = null
) : Parcelable

val PhotoUrls.smallestAvailable: String?
    get() = thumb ?: small ?: raw ?: regular ?: full
