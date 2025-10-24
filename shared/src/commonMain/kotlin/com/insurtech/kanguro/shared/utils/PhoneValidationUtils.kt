package com.insurtech.kanguro.shared.utils

/**
 * Validates US phone numbers. Accepts formats like (123) 456-7890, 123-456-7890, 1234567890, +1 123 456 7890, etc.
 */
fun isValidUSPhone(phone: String): Boolean {
    val cleaned = phone.replace("[\\s()-]".toRegex(), "")
    val usPattern = "^(\\+1)?[2-9][0-9]{2}[2-9][0-9]{6}$".toRegex()
    return usPattern.matches(cleaned)
}