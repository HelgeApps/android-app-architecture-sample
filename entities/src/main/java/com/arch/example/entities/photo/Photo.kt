package com.arch.example.entities.photo

import java.time.Instant

data class Photo(
    val id: String,
    val description: String?,
    val altDescription: String?,
    val createdAt: Instant,
    val color: String?,
    val photoUrls: PhotoUrls,
    val width: Int?,
    val height: Int?
)
