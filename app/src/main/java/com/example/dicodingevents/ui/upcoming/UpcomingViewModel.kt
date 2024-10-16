package com.example.dicodingevents.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.data.response.EventsResponse
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.ui.finished.FinishedViewModel
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class UpcomingViewModel : ViewModel() {
    private val _listUpcoming = MutableLiveData<List<DicodingEvent>>()
    val listUpcoming: LiveData<List<DicodingEvent>> = _listUpcoming
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    init {
        getUpcoming()
    }

    companion object{
        private const val TAG = "UpcomingViewModel"
    }

    internal fun getUpcoming() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(1)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isError.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.listEvents.isEmpty()) {
                            _isEmpty.value = true
                        } else {
                            _listUpcoming.value = responseBody.listEvents
                            _isEmpty.value = false
                        }
                    }
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(UpcomingViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }

    internal fun getUpcomingWithQuery(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventsWithQuery(1, q)
        client.enqueue(object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _isError.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.listEvents.isEmpty()) {
                            _isEmpty.value = true
                        } else {
                            _listUpcoming.value = responseBody.listEvents
                            _isEmpty.value = false
                        }
                    }
                } else {
                    _isError.value = true
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(UpcomingViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }
}