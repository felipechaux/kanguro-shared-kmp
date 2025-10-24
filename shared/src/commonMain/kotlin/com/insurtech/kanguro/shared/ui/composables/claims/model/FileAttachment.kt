package com.insurtech.kanguro.shared.ui.composables.claims.model

// Data class for file info
data class FileAttachment(
    val fileName: String,
    val fileByteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as FileAttachment

        if (fileName != other.fileName) return false
        if (!fileByteArray.contentEquals(other.fileByteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + fileByteArray.contentHashCode()
        return result
    }
}
