package com.example.dicodingevents.data.remote.response

import com.google.gson.annotations.SerializedName

data class DicodingEventDetailResponse(
	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("event")
	val event: DicodingEvent
)

