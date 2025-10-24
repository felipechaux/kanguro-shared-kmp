package com.insurtech.kanguro.shared.data.questSystem.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateQuestProgressRequest(
    @SerialName("ci_customer_id")
    val customerId: String,
    @SerialName("quest_id")
    val questId: String,
    val status: String
)
