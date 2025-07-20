// File: DateUtils.kt
package com.example.finedout.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getCurrentMonthYear(): String {
        val dateFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        return dateFormat.format(Date()) // e.g., "July 2025"
    }
}
