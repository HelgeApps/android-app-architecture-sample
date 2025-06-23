package com.arch.example.network.adapters

import com.arch.example.network.utils.OauthGrantType
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.Locale

class OauthGrantTypeAdapter {
    @ToJson
    fun toJson(type: OauthGrantType?): String? = type?.name?.lowercase(Locale.ENGLISH)

    @FromJson
    fun fromJson(type: String?): OauthGrantType? = OauthGrantType.entries.firstOrNull {
        it.name.lowercase(Locale.ENGLISH) == type
    }
}