package com.arch.example.entities.auth

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

// next free proto number - 4

@Serializable
data class AuthPrefsData(
    @ProtoNumber(1) val accessToken: String? = null,
    @ProtoNumber(2) val tokenType: String? = null,
    @ProtoNumber(3) val refreshToken: String? = null // TODO: no documentation about refresh endpoint
)