package com.arch.example.domain

import com.arch.example.entities.topic.Topic
import com.arch.example.data.TopicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveTopicUseCase @Inject constructor(
    private val topicRepository: TopicRepository
) {
    operator fun invoke(id: String): Flow<Topic> {
        return topicRepository.observeTopic(id = id)
    }
}
