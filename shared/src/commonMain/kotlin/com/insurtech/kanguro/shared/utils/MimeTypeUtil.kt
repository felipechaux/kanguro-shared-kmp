package com.insurtech.kanguro.shared.utils

object MimeTypeUtil {
    fun getMimeType(fileName: String): String {
        return when (fileName.substringAfterLast('.', "").lowercase()) {
            "png" -> "image/png"
            "jpg", "jpeg" -> "image/jpeg"
            "HEIC", "heic" -> "image/heic"
            "HEIF", "heif" -> "image/heif"
            "gif" -> "image/gif"
            "bmp" -> "image/bmp"
            "webp" -> "image/webp"
            "pdf" -> "application/pdf"
            "txt" -> "text/plain"
            "mp4" -> "video/mp4"
            "mp3" -> "audio/mpeg"
            // Add more as needed
            else -> "application/octet-stream" // Default fallback
        }
    }
}
