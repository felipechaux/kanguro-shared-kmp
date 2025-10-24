package com.insurtech.kanguro.shared.data.questSystem.repository

import com.insurtech.kanguro.shared.data.questSystem.api.QuestSystemService
import com.insurtech.kanguro.shared.data.questSystem.api.response.QuestProgressResult
import com.insurtech.kanguro.shared.data.questSystem.api.response.QuestResult
import com.insurtech.kanguro.shared.data.questSystem.api.response.UpdateQuestProgressResult

class QuestSystemRepository(private val questSystemService: QuestSystemService) {

    suspend fun getProgress(): QuestProgressResult {
        return questSystemService.getProgress()
    }

    suspend fun getAvailableQuests(language: String): QuestResult {
        return questSystemService.getAvailableQuests(language)
    }

    suspend fun updateQuestProgress(questId: String, status: String): UpdateQuestProgressResult {
        return questSystemService.updateQuestProgress(questId, status)
    }

    suspend fun createQuestProgress(customerId: String, questId: String, status: String): UpdateQuestProgressResult {
        return questSystemService.createQuestProgress(customerId, questId, status)
    }
}
