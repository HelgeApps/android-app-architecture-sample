package com.arch.example.domain

import androidx.paging.PagingData
import com.arch.example.entities.photo.Photo
import com.arch.example.data.TopicRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopicPhotosUseCase @Inject constructor(
    private val topicRepository: TopicRepository
) {
    operator fun invoke(topicId: String): Flow<PagingData<Photo>> {
        return topicRepository.getTopicPhotos(topicId = topicId)
    }
}
