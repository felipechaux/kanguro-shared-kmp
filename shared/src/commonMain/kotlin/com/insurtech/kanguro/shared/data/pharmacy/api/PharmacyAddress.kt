package com.insurtech.kanguro.shared.data.pharmacy.api

import kotlinx.serialization.Serializable

@Serializable
data class PharmacyAddress(
    val name: String,
    val streetAddress1: String,
    val streetAddress2: String,
    val city: String,
    val state: String,
    val zip: String,
    val country: String,
    val isPrimary: Boolean
)
