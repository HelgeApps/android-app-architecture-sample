package com.arch.example.network.converters

import com.arch.example.entities.topic.TopicsOrder
import retrofit2.Converter
import javax.inject.Inject

class TopicsOrderConverter @Inject constructor() : Converter<TopicsOrder, String> {
    override fun convert(order: TopicsOrder): String {
        return order.type
    }
}
