package com.arch.example.entities

import com.arch.example.entities.topic.TopicsOrder
import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

// next free proto number - 2

@Serializable
data class Preferences(
    @ProtoNumber(1) val topicsOrder: TopicsOrder = TopicsOrder.POSITION
)
