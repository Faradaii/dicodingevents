package com.example.dicodingevents.ui.finished

import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.DicodingEventRepository

class FinishedViewModel(private val dicodingEventRepository: DicodingEventRepository) : ViewModel() {

    fun getFinishedEvents() = dicodingEventRepository.getAllDicodingEvents(isUpcoming = false)

}