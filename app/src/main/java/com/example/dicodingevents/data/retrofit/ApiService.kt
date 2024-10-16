package com.example.dicodingevents.data.retrofit

import com.example.dicodingevents.data.response.EventDetailResponse
import com.example.dicodingevents.data.response.EventsResponse
import retrofit2.http.*
import retrofit2.Call

interface ApiService {
    @GET("events")
    fun getEvents(
        @Query("active") active: Int
    ): Call<EventsResponse>

    @GET("events")
    fun getEventsWithQuery(
        @Query("active") active: Int,
        @Query("q") query: String
    ): Call<EventsResponse>

    @GET("events/{id}")
    fun getEventDetail(
        @Path("id") id: Int
    ): Call<EventDetailResponse>
}