package com.arch.example.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkError(
    @Json(name = "errors")
    val errors: List<String>?,

    @Json(name = "message")
    val error: String?
)
