package com.insurtech.kanguro.shared.data.questSystem.api.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateQuestProgressRequest(
    val status: String
)

@Serializable
data class UpdateQuestProgressResult(
    @SerialName("data")
    val data: QuestSystemResult
)

@Serializable
data class QuestSystemResult(
    val id: String,
    @SerialName("ci_customer_id")
    val customerId: String,
    @SerialName("quest_id")
    val questId: String,
    val status: String
)
