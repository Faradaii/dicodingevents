package com.example.dicodingevents.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevents.data.response.DicodingEvent
import com.example.dicodingevents.data.response.EventsResponse
import com.example.dicodingevents.data.retrofit.ApiConfig
import com.example.dicodingevents.ui.finished.FinishedViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _listFinished = MutableLiveData<List<DicodingEvent>>()
    val listFinished: LiveData<List<DicodingEvent>> = _listFinished
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError
    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean> = _isEmpty

    init {
        getFinished()
    }

    companion object{
        private const val TAG = "FinishedViewModel"
    }

    internal fun getFinished() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEvents(0)
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
                            _listFinished.value = responseBody.listEvents
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
                Log.e(FinishedViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }

    internal fun getFinishedWithQuery(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventsWithQuery(0, q)
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
                            _listFinished.value = responseBody.listEvents
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
                Log.e(FinishedViewModel.TAG, "onFailure: ${t.message}")
            }
        })
    }
}