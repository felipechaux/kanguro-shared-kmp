package com.insurtech.kanguro.shared.ui.composables.questSystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insurtech.kanguro.shared.data.questSystem.model.Quest
import com.insurtech.kanguro.shared.data.questSystem.model.QuestStatus
import com.insurtech.kanguro.shared.data.questSystem.repository.QuestSystemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class QuestSystemViewModel(
    private val customerId: String,
    private val appLanguage: String
) : ViewModel(), KoinComponent {
    private val questSystemRepository: QuestSystemRepository by inject()

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        getQuests()
    }

    fun refreshQuests() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                getQuests()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to refresh quests"
                )
            } finally {
                _state.value = _state.value.copy(loading = false)
            }
        }
    }

    fun createQuestProgress(questId: String, status: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                val questResponse = questSystemRepository.getProgress()
                val existingProgress = questResponse.data.find {
                    it.customerId == customerId && it.questId.id == questId
                }
                if (existingProgress == null) {
                    questSystemRepository.createQuestProgress(customerId, questId, status)
                } else {
                    updateQuestProgress(existingProgress.id, status)
                }
                getQuests()
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to create/update progress"
                )
            } finally {
                _state.value = _state.value.copy(loading = false)
            }
        }
    }

    private fun updateQuestProgress(id: String, status: String) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                questSystemRepository.updateQuestProgress(id, status)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to update quest progress"
                )
            } finally {
                _state.value = _state.value.copy(loading = false)
            }
        }
    }

    private fun getQuests() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            try {
                // Get available quests and user progress
                val availableQuests = questSystemRepository.getAvailableQuests(appLanguage)
                val userProgress = questSystemRepository.getProgress()

                // Create a map of completed quests for quick lookup, filtered by customer ID
                val completedQuestsMap = userProgress.data
                    .filter { it.customerId == customerId }
                    .associateBy { it.questId.id }

                // Convert available quests to QuestSystem format while maintaining order
                val allQuests = availableQuests.data.map { questData ->
                    val existingProgress = completedQuestsMap[questData.id]
                    val translation = questData.translations.first()

                    if (existingProgress != null) {
                        // If quest is in progress, use the existing progress
                        Quest(
                            id = questData.id,
                            title = translation.title,
                            titleDescription = translation.titleDescription,
                            subTitle = translation.subTitle,
                            subTitleDescription = translation.subTitleDescription,
                            primaryButtonText = translation.primaryButtonText,
                            icon = questData.icon,
                            action = questData.action,
                            customerId = existingProgress.customerId,
                            status = when (existingProgress.status) {
                                "COMPLETED" -> QuestStatus.COMPLETED
                                "CLAIMED" -> QuestStatus.CLAIMED
                                else -> QuestStatus.PENDING
                            }
                        )
                    } else {
                        // If quest is not in progress, create a new pending quest
                        Quest(
                            id = questData.id,
                            title = translation.title,
                            titleDescription = translation.titleDescription,
                            subTitle = translation.subTitle,
                            subTitleDescription = translation.subTitleDescription,
                            primaryButtonText = translation.primaryButtonText,
                            icon = questData.icon,
                            action = questData.action,
                            customerId = customerId,
                            status = QuestStatus.PENDING
                        )
                    }
                }

                _state.value = _state.value.copy(
                    quests = allQuests,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Failed to load quests"
                )
            } finally {
                _state.value = _state.value.copy(loading = false)
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val quests: List<Quest> = emptyList(),
        val error: String? = null
    )
}
