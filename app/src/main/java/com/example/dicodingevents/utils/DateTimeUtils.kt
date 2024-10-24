package com.example.dicodingevents.utils

import java.text.SimpleDateFormat
import java.util.Locale

object DateTimeUtils {

    fun formatDateWithTimezone(inputDate: String): String? {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val dateFormatted = inputFormat.parse(inputDate)

            val outputFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a 'WIB'", Locale.getDefault())
            return dateFormatted?.let { outputFormat.format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}