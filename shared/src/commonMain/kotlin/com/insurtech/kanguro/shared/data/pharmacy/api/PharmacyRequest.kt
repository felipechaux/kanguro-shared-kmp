package com.insurtech.kanguro.shared.data.pharmacy.api

import kotlinx.serialization.Serializable

@Serializable
data class PharmacyRequest(
    val userName: String,
    val firstName: String,
    val lastName: String,
    val expirationWindow: String,
    val addresses: List<PharmacyAddress>
)
