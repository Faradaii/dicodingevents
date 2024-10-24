package com.example.dicodingevents.ui.upcoming

import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.DicodingEventRepository

class UpcomingViewModel(private val dicodingEventRepository: DicodingEventRepository) : ViewModel() {

    fun getUpcomingEvents() = dicodingEventRepository.getAllDicodingEvents(isUpcoming = true)

}