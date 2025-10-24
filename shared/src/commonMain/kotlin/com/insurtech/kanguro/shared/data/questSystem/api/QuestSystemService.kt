package com.insurtech.kanguro.shared.data.questSystem.api

import com.insurtech.kanguro.shared.data.questSystem.api.request.CreateQuestProgressRequest
import com.insurtech.kanguro.shared.data.questSystem.api.response.QuestProgressResult
import com.insurtech.kanguro.shared.data.questSystem.api.response.QuestResult
import com.insurtech.kanguro.shared.data.questSystem.api.response.UpdateQuestProgressRequest
import com.insurtech.kanguro.shared.data.questSystem.api.response.UpdateQuestProgressResult
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class QuestSystemService(
    private val client: HttpClient
) {

    suspend fun getProgress(): QuestProgressResult {
        return client.get("/items/customer_quest_progress?fields=*,quest_id.*,quest_id.translations.*") {
        }.body<QuestProgressResult>()
    }

    suspend fun getAvailableQuests(language: String): QuestResult {
        return client.get("/items/quest?fields=*,translations.*&deep[translations][_filter][languages_code][_eq]=$language") {
        }.body<QuestResult>()
    }

    suspend fun updateQuestProgress(id: String, status: String): UpdateQuestProgressResult {
        return client.patch("/items/customer_quest_progress/$id") {
            header("Content-Type", "application/json")
            setBody(UpdateQuestProgressRequest(status))
        }.body<UpdateQuestProgressResult>()
    }

    suspend fun createQuestProgress(customerId: String, questId: String, status: String): UpdateQuestProgressResult {
        return client.post("/items/customer_quest_progress") {
            header("Content-Type", "application/json")
            setBody(CreateQuestProgressRequest(customerId, questId, status))
        }.body<UpdateQuestProgressResult>()
    }
}
