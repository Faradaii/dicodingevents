package com.example.dicodingevents.data.remote.retrofit

import com.example.dicodingevents.data.remote.response.DicodingEventDetailResponse
import com.example.dicodingevents.data.remote.response.EventsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active") active: Int,
        @Query("limit") limit: Int?
    ): EventsResponse

    @GET("events")
    suspend fun getEventsWithQuery(
        @Query("active") active: Int,
        @Query("q") query: String
    ): EventsResponse

    @GET("events/{id}")
    suspend fun getEventDetail(
        @Path("id") id: Int
    ): DicodingEventDetailResponse
}