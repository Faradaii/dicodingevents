package com.example.dicodingevents.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object DateTimeUtils {
    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"

    fun formatDateWithTimezone(inputDate: String): String? {
        return try {
            val inputFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

            val dateFormatted = inputFormat.parse(inputDate)

            val outputFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a 'WIB'", Locale.getDefault())
            return dateFormatted?.let { outputFormat.format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun formatDateShorter(inputDate: String): String? {
        val inputFormatter = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        val outputFormatter = SimpleDateFormat("EEE, MMM d • h:mm a 'WIB'", Locale.ENGLISH)

        val date: Date? = inputFormatter.parse(inputDate)

        val formattedDate = date?.let { outputFormatter.format(it) }
        return formattedDate
    }

    fun isUpcomingChecker(eventDateTime: String): Boolean {
        val dateFormat = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")

        return try {
            val eventDate = dateFormat.parse(eventDateTime)
            val currentDate = Calendar.getInstance(TimeZone.getTimeZone("Asia/Jakarta")).time
            val diffInMillis = eventDate?.time?.minus(currentDate.time) ?: 0
            val diffInMinutes = diffInMillis / (1000 * 60)
            diffInMinutes > 0
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}