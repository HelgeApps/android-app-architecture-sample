package com.arch.example.network.adapters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class DateTimeTypeAdapter {
    @ToJson
    fun toJson(instant: Instant): String {
        return instant.toString()
    }

    @FromJson
    fun fromJson(isoOffsetDateTime: String): Instant {
        return Instant.parse(isoOffsetDateTime)
    }
}