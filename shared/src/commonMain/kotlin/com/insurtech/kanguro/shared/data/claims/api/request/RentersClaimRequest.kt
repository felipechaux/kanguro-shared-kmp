package com.insurtech.kanguro.shared.data.claims.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RentersClaimRequest(
    @SerialName("customer_description")
    val customerDescription: String,
    @SerialName("date_of_loss")
    val dateOfLoss: String,
    @SerialName("policy_id_ci")
    val policyIdCi: String,
    @SerialName("latest_phone_number")
    val latestPhoneNumber: String,
    val attachments: Attachments? = null
) {
    @Serializable
    data class Attachments(
        val create: List<Create>
    )

    @Serializable
    data class Create(
        @SerialName("directus_files_id")
        val directusFilesId: String
    )
}
