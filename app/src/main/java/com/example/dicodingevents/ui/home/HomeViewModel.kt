package com.example.dicodingevents.ui.home

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.data.response.EventsResponse
import com.example.dicodingevents.data.retrofit.ApiConfig

class HomeViewModel : ViewModel() {
    private val _listFinished = MutableLiveData<List<DicodingEvent>>()
    val listFinished: LiveData<List<DicodingEvent>> = _listFinished
    private val _listUpcoming = MutableLiveData<List<DicodingEvent>>()
    val listUpcoming: LiveData<List<DicodingEvent>> = _listUpcoming
    private val _isUpcomingLoading = MutableLiveData<Boolean>()
    val isUpcomingLoading: LiveData<Boolean> = _isUpcomingLoading
    private val _isFinishedLoading = MutableLiveData<Boolean>()
    val isFinishedLoading: LiveData<Boolean> = _isFinishedLoading
    private val _isUpcomingError = MutableLiveData<Boolean>()
    val isUpcomingError: LiveData<Boolean> = _isUpcomingError
    private val _isFinishedError = MutableLiveData<Boolean>()
    val isFinishedError: LiveData<Boolean> = _isFinishedError
    private val _isUpcomingEmpty = MutableLiveData<Boolean>()
    val isUpcomingEmpty: LiveData<Boolean> = _isUpcomingEmpty
    private val _isFinishedEmpty = MutableLiveData<Boolean>()
    val isFinishedEmpty: LiveData<Boolean> = _isFinishedEmpty

    init {
        getUpcoming()
        getFinished()
    }

    private fun getUpcoming() {
        _isUpcomingLoading.value = true
        val client = ApiConfig.getApiService().getEvents(1)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isUpcomingLoading.value = false
                if (response.isSuccessful) {
                    _isUpcomingError.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.listEvents.isEmpty()) {
                            _isUpcomingEmpty.value = true
                        } else {
                            _listUpcoming.value = if ( responseBody.listEvents.size > 4 ) responseBody.listEvents.slice(0..4) else responseBody.listEvents
                            _isUpcomingEmpty.value = false
                        }
                    }
                } else {
                    _isUpcomingError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isUpcomingLoading.value = false
                _isUpcomingError.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun getFinished() {
        _isFinishedLoading.value = true
        val client = ApiConfig.getApiService().getEvents(0)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isFinishedLoading.value = false
                if (response.isSuccessful) {
                    _isFinishedError.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.listEvents.isEmpty()) {
                            _isFinishedEmpty.value = true
                        } else {
                            _listFinished.value = if ( responseBody.listEvents.size > 4 ) responseBody.listEvents.slice(0..4) else responseBody.listEvents
                            _isFinishedEmpty.value = false
                        }
                    }
                } else {
                    _isFinishedError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isFinishedLoading.value = false
                _isFinishedError.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "HomeViewModel"
    }
}