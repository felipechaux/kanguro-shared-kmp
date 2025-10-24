package com.insurtech.kanguro.shared.data.claims.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RentersClaimResponse(
    val data: RentersClaim
)

@Serializable
data class RentersClaim(
    val id: String,
    val status: String,
    @SerialName("customer_description")
    val customerDescription: String,
    @SerialName("date_of_loss")
    val dateOfLoss: String,
    @SerialName("policy_id_ci")
    val policyIdCi: String
)
