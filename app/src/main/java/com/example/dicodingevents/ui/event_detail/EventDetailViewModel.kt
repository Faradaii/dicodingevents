package com.example.dicodingevents.ui.event_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevents.data.DicodingEventRepository
import com.example.dicodingevents.data.local.entity.DicodingEventEntity
import kotlinx.coroutines.launch

class EventDetailViewModel(private val dicodingEventRepository: DicodingEventRepository) : ViewModel() {

    fun getDetailEvent(id: Int) = dicodingEventRepository.getDetailDicodingEvent(id)

    fun favoriteDicodingEvent(dicodingEvent: DicodingEventEntity) {
        viewModelScope.launch {
            dicodingEventRepository.setDicodingEventFavorite(dicodingEvent, favoriteState = true)
        }
    }

    fun unFavoriteDicodingEvent(dicodingEvent: DicodingEventEntity) {
        viewModelScope.launch {
            dicodingEventRepository.setDicodingEventFavorite(dicodingEvent, favoriteState = false)
        }
    }

    fun viewedDicodingEvent(dicodingEvent: DicodingEventEntity) {
        viewModelScope.launch {
            dicodingEventRepository.setDicodingEventViewed(dicodingEvent, viewedState = true)
        }
    }

}