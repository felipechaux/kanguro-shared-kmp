package com.insurtech.kanguro.shared.data.questSystem.api.response

import com.insurtech.kanguro.shared.data.questSystem.model.EmptyQuest
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestProgressResult(
    val data: List<QuestSystem>
)

@Serializable
data class QuestSystem(
    val id: String,
    @SerialName("ci_customer_id")
    val customerId: String,
    val status: String,
    @SerialName("quest_id")
    val questId: EmptyQuest
)
