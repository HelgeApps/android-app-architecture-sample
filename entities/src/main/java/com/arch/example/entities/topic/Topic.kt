package com.arch.example.entities.topic

import android.os.Parcelable
import androidx.annotation.Keep
import com.arch.example.entities.photo.CoverPhoto
import kotlinx.parcelize.Parcelize
import java.time.Instant

// keep models which you pass as argument using Navigation Component
// https://developer.android.com/guide/navigation/navigation-pass-data#use_keep_annotations
@Keep
@Parcelize
data class Topic(
    val id: String,
    val title: String,
    val description: String?,
    val publishedAt: Instant,
    val coverPhoto: CoverPhoto?
) : Parcelable