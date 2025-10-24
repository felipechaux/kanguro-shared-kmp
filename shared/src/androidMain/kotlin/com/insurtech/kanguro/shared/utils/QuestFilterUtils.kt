package com.insurtech.kanguro.shared.utils

import com.insurtech.kanguro.shared.data.questSystem.model.Quest
import com.insurtech.kanguro.shared.data.questSystem.model.QuestValues
import com.insurtech.kanguro.shared.utils.PostHogUtils.CALL_US_NOW_VARIANT
import com.insurtech.kanguro.shared.utils.PostHogUtils.POSTHOG_ONBOARDING_CALL_FLAG
import com.insurtech.kanguro.shared.utils.PostHogUtils.SCHEDULE_CALL_VARIANT
import com.posthog.PostHog

object QuestFilterUtils {
    fun filterOnboardingQuests(quests: List<Quest>): List<Quest> {
        return if (PostHog.isFeatureEnabled(POSTHOG_ONBOARDING_CALL_FLAG)) {
            when (PostHog.getFeatureFlag(POSTHOG_ONBOARDING_CALL_FLAG)) {
                CALL_US_NOW_VARIANT -> quests.filter {
                    it.id == QuestValues.OnboardingCall.name || (it.id != QuestValues.OnboardingCall.name && it.id != QuestValues.OnboardingCallB.name)
                }
                SCHEDULE_CALL_VARIANT -> quests.filter {
                    it.id == QuestValues.OnboardingCallB.name || (it.id != QuestValues.OnboardingCall.name && it.id != QuestValues.OnboardingCallB.name)
                }
                else -> quests.filter { it.id != QuestValues.OnboardingCall.name && it.id != QuestValues.OnboardingCallB.name }
            }
        } else {
            quests.filter { it.id != QuestValues.OnboardingCall.name && it.id != QuestValues.OnboardingCallB.name }
        }
    }

    fun filterQuestByFlag(quests: List<Quest>, posthogFlag: String, questValue: QuestValues): List<Quest> {
        return if (PostHog.isFeatureEnabled(posthogFlag)) {
            quests
        } else {
            quests.filter { it.id != questValue.name }
        }
    }
}
