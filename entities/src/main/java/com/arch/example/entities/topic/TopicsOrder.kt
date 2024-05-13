package com.arch.example.entities.topic

import androidx.annotation.Keep

@Keep
enum class TopicsOrder(val type: String) {
    FEATURED("featured"),
    LATEST("latest"),
    OLDEST("oldest"),
    POSITION("position")
}