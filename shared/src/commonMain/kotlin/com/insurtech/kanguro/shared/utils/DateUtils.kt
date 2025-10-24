package com.insurtech.kanguro.shared.utils

import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object DateUtils {

    @OptIn(ExperimentalTime::class)
    fun formatIsoToYyyyMmDd(dateString: String?): String {
        return try {
            if (dateString.isNullOrBlank()) return "-"
            val normalized = if (
                dateString.endsWith("Z") ||
                dateString.contains("+") ||
                (dateString.lastIndexOf('-') > 9) // handle negative offset, not the date part
            ) {
                dateString
            } else {
                dateString + "Z"
            }
            val instant = Instant.parse(normalized)
            instant.toString().substring(0, 10)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}