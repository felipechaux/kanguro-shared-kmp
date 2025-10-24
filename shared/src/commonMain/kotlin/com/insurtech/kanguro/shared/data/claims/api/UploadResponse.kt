package com.insurtech.kanguro.shared.data.claims.api

import kotlinx.serialization.Serializable

@Serializable
data class UploadData(
    val id: String
)

@Serializable
data class UploadResponse(
    val data: UploadData
)
