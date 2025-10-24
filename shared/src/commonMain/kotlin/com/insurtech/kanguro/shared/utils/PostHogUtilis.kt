package com.insurtech.kanguro.shared.utils

object PostHogUtils {
    const val POSTHOG_QUEST_SYSTEM_EVENT = "questSystemInteraction"
    const val POSTHOG_QUEST_NAME_ID = "questNameID"
    const val POSTHOG_QUEST_STATUS = "questStatus"
    const val POSTHOG_QUEST_ACTION = "questAction"
    const val POSTHOG_QUEST_OPENED_ACTION = "opened"
    const val POSTHOG_ONBOARDING_CALL_FLAG = "inbound-contact"
    const val POSTHOG_MEDICAL_HISTORY_FLAG = "app_quest_medical"
    const val POSTHOG_AIRVET_FLAG = "app_quest_airvet"
    const val POSTHOG_PETTER_FLAG = "app_petter"
    const val CALL_US_NOW_VARIANT = "call_us_now"
    const val SCHEDULE_CALL_VARIANT = "schedule_call"
    const val POSTHOG_ARREARS_BANNER_VIEWED = "arrearsBannerViewed"
    const val POSTHOG_ARREARS_BANNER_CLICKED = "arrearsBannerClicked"
    const val POSTHOG_ARREARS_PAYMENT_METHOD_CLOSED = "arrearsPaymentMethodClosed"
    const val POSTHOG_VET_CONNECTION_AIRVET_OPENED = "vetConnectionAirvetOpened"
    const val POSTHOG_VET_CONNECTION_SIGN_UP_CLICKED = "vetConnectionSignUpClicked"
    const val POSTHOG_VET_CONNECTION_FIND_VET_CLICKED = "vetConnectionFindVetClicked"
}
