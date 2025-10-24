package com.insurtech.kanguro.shared.ui.composables.questSystem

import com.insurtech.kanguro.shared.data.questSystem.model.QuestValues

sealed class QuestSystemEvent {
    data class OnLiveVetPressed(val questId: String = QuestValues.Airvet.name) : QuestSystemEvent()
    data class OnPetterPressed(val questId: String = QuestValues.Petter.name) : QuestSystemEvent()
    data class OnMedicalHistoryPressed(val questId: String = QuestValues.MedicalHistory.name) : QuestSystemEvent()
    data class OnOnboardingCallPressed(val url: String) : QuestSystemEvent()
    data class Referral(val questId: String = QuestValues.Referral.name) : QuestSystemEvent()
}
