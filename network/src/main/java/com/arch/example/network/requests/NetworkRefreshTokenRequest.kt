package com.arch.example.network.requests

import com.arch.example.network.utils.OauthGrantType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkRefreshTokenRequest(
    @Json(name = "refresh_token")
    val refreshToken: String,

    @Json(name = "grant_type")
    val grantType: OauthGrantType
)
