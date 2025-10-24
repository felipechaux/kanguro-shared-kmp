package com.insurtech.kanguro.shared.data.claims.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RentersClaimsListResponse(
    val data: List<RentersClaimSubmitted>
)

@Serializable
data class RentersClaimSubmitted(
    val id: String,
    val status: String,
    @SerialName("date_created")
    val dateCreated: String?,
    @SerialName("date_of_loss")
    val dateOfLoss: String?,
    @SerialName("policy_id_ci")
    val policyIdCi: String?,
    @SerialName("code")
    val code: String?
)
