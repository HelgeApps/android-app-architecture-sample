package com.arch.example.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arch.example.data.PhotoRepository
import com.arch.example.ui.utils.WhileUiSubscribed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    photoRepository: PhotoRepository
) : ViewModel() {

    val photosResponse = photoRepository.getPhotos()
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = PagingData.empty()
        )
}
