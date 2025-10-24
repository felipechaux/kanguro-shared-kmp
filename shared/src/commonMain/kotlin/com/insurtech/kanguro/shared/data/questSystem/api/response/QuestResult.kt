package com.insurtech.kanguro.shared.data.questSystem.api.response

import com.insurtech.kanguro.shared.data.questSystem.model.EmptyQuest
import kotlinx.serialization.Serializable

@Serializable
data class QuestResult(
    val data: List<EmptyQuest>
)
