package com.arch.example.network.requests

import com.arch.example.network.utils.OauthGrantType
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkLoginRequest(
    @Json(name = "client_id")
    val clientId: String,

    @Json(name = "client_secret")
    val clientSecret: String,

    @Json(name = "redirect_uri")
    val redirectUri: String,

    @Json(name = "code")
    val code: String,

    @Json(name = "grant_type")
    val grantType: OauthGrantType
)
