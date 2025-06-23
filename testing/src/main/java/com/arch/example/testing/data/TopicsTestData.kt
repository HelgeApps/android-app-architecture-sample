package com.arch.example.testing.data

import com.arch.example.entities.photo.CoverPhoto
import com.arch.example.entities.photo.PhotoUrls
import com.arch.example.entities.topic.Topic
import java.time.Instant

val topicsTestData: List<Topic> = listOf(
    Topic(
        id = "WdChqlsJN9c",
        title = "Black & White",
        description = "Discover the timeless allure of black and white photography. Embrace contrast, texture, and emotion in every monochromatic photo you capture.",
        publishedAt = Instant.parse("2024-03-26T20:36:24Z"),
        coverPhoto = CoverPhoto(
            photoUrls = PhotoUrls(
                regular = "https://images.unsplash.com/photo-1680900009683-a822934b058f?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max"
            )
        ),
    ),
    Topic(
        id = "E--_pnIirG4",
        title = "Archival",
        description = "The Archival Topic explores captivating art and photography from renowned galleries, museums, and cultural institutions around the world who use Unsplash to showcase their archives.  Journey through the pages of history, catching glimpses of our ancestors' stories, struggles, triumphs, and the timeless beauty of creative expression.",
        publishedAt = Instant.parse("2024-01-25T06:47:02Z"),
        coverPhoto = CoverPhoto(
            photoUrls = PhotoUrls(
                regular = "https://images.unsplash.com/photo-1704120103349-f9db80e2cd6e?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=1080&fit=max"
            )
        ),
    ),
)
