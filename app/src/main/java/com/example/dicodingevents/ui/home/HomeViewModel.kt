package com.example.dicodingevents.ui.home

import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.DicodingEventRepository

class HomeViewModel(private val dicodingEventRepository: DicodingEventRepository) : ViewModel() {

    fun getUpcomingEvents() = dicodingEventRepository.getAllDicodingEvents(isUpcoming = true, limit = 5)

    fun getFinishedEvents() = dicodingEventRepository.getAllDicodingEvents(isUpcoming = false, limit = 5)

}