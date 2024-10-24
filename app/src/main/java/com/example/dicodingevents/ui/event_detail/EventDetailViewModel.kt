package com.example.dicodingevents.ui.event_detail

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.data.response.EventDetailResponse
import com.example.dicodingevents.data.retrofit.ApiConfig

class EventDetailViewModel : ViewModel() {
    private val _detailEvent = MutableLiveData<DicodingEvent>()
    val detailEvent: LiveData<DicodingEvent> = _detailEvent
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    internal fun getDetailEvent(eventId: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventDetail(eventId)
        Log.d(TAG,("getDetailEvent: $eventId"))
        client.enqueue(object : Callback<EventDetailResponse> {
            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isError.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _detailEvent.value = responseBody.event
                    }
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "EventDetailViewModel"
    }
}