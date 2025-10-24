package com.insurtech.kanguro.shared.data.questSystem.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestTranslation(
    val id: Int,
    @SerialName("quest_id")
    val questId: String,
    @SerialName("languages_code")
    val languagesCode: String,
    val title: String,
    @SerialName("title_description")
    val titleDescription: String,
    @SerialName("sub_title")
    val subTitle: String,
    @SerialName("subtitle_description")
    val subTitleDescription: String,
    @SerialName("primary_button")
    val primaryButtonText: String
)

@Serializable
data class EmptyQuest(
    val id: String,
    val icon: String,
    val action: String? = null,
    val translations: List<QuestTranslation> = emptyList()
)

data class Quest(
    val id: String,
    val title: String,
    val titleDescription: String,
    val subTitle: String,
    val subTitleDescription: String,
    val primaryButtonText: String,
    var icon: String,
    val action: String? = null,
    var status: QuestStatus = QuestStatus.PENDING,
    val customerId: String? = null
)
