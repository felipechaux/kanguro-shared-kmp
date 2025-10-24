package com.insurtech.kanguro.shared.data.claims.api.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserProfileRequest(
    val givenName: String,
    val surname: String,
    val phone: String
)