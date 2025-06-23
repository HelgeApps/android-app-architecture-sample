package com.arch.example.domain

import com.arch.example.common.result.AsyncResult
import com.arch.example.common.result.asResult
import com.arch.example.entities.topic.TopicsOrder
import com.arch.example.data.TopicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchTopicsUseCase @Inject constructor(
    private val topicRepository: TopicRepository
) {
    operator fun invoke(order: TopicsOrder): Flow<AsyncResult<Unit>> {
        return topicRepository.fetchAndSaveTopics(order = order)
            .asResult()
    }
}
