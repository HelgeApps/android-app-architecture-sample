package com.arch.example.testing.data

import com.arch.example.entities.photo.Photo
import com.arch.example.entities.photo.PhotoUrls
import java.time.Instant

val topicPhotosTestData: List<Photo> = listOf(
    Photo(
        id = "SvrlzbefjiU",
        color = "#262626",
        description = "Discover the timeless allure of black and white photography. Embrace contrast, texture, and emotion in every monochromatic photo you capture.",
        altDescription = null,
        createdAt = Instant.parse("2024-04-25T16:11:10Z"),
        photoUrls = PhotoUrls(
            regular = "https://images.unsplash.com/photo-1714060334882-41e92bd8f73b?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNjIxNDR8MHwxfGFsbHwxfHx8fHx8Mnx8MTcxNDE0MjkzNnw&ixlib=rb-4.0.3&q=80&w=1080\""
        ),
        width = 3448,
        height = 5168,
    ),
    Photo(
        id = "r5F6oYzyhCo",
        color = "#0c2640",
        description = "Deserted petrol station",
        altDescription = "a gas station lit up at night with neon lights",
        createdAt = Instant.parse("2024-04-22T17:59:07Z"),
        photoUrls = PhotoUrls(
            regular = "https://images.unsplash.com/photo-1713807868932-f7a77576ac5a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=M3wyNjIxNDR8MHwxfGFsbHwyfHx8fHx8Mnx8MTcxNDE0MjkzNnw&ixlib=rb-4.0.3&q=80&w=1080"
        ),
        width = 3360,
        height = 5040,
    ),
)