package com.arch.example.network.models.auth

import com.arch.example.entities.auth.AuthPrefsData
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkLoginResponse(
    @Json(name = "access_token")
    val accessToken: String,

    @Json(name = "token_type")
    val tokenType: String,

    @Json(name = "refresh_token")
    val refreshToken: String
)

fun NetworkLoginResponse.asExternalModel() = AuthPrefsData(
    accessToken = this.accessToken,
    tokenType = this.tokenType,
    refreshToken = this.refreshToken
)