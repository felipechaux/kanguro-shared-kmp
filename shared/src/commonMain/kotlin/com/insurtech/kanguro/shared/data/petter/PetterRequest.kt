package com.insurtech.kanguro.shared.data.petter

import kotlinx.serialization.Serializable

@Serializable
data class PetterRequest(
    val phone: String,
    val customerId: String,
    val firstName: String,
    val lastName: String,
    val email: String
)
