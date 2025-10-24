package com.insurtech.kanguro.shared

object Constants {

    // Language Constants
    const val APP_LANGUAGE_ES = "es-ES"
    const val APP_LANGUAGE_EN = "en-US"

    // External Browser
    const val PETTER_EXTERNAL_BROWSER = "kanguro://openLinkInBrowser"

    // DI Property Keys
    const val PROPERTY_QUEST_SYSTEM_HOST = "questSystemHost"
    const val PROPERTY_QUEST_SYSTEM_AUTH_TOKEN = "questSystemAuthToken"

    // Pharmacy DI Property Keys
    const val PROPERTY_PHARMACY_HOST = "pharmacyHost"
    const val PROPERTY_PHARMACY_AUTH_TOKEN = "pharmacyAuthToken"
    const val PROPERTY_KANGURO_ACCESS_TOKEN = "kanguroAccessToken"
    const val PROPERTY_KANGURO_REFRESH_TOKEN = "kanguroRefreshToken"
    const val PROPERTY_KANGURO_HOST = "kanguroHost"
    const val PROPERTY_KANGURO_API_KEY = "kanguroApiKey"

    // HTTP Headers
    const val HEADER_AUTHORIZATION = "Authorization"
    const val HEADER_BEARER_PREFIX = "Bearer"
    const val HEADER_API_KEY = "api_key"

    // DI Qualifier Names
    const val DI_QUALIFIER_PROFILE_JSON = "profileJson"
    const val DI_QUALIFIER_AUTH_HTTP_CLIENT = "authHttpClient"
    const val DI_QUALIFIER_PROFILE_HTTP_CLIENT = "profileHttpClient"
    const val DI_QUALIFIER_DIRECTUS_JSON = "directusJson"
    const val DI_QUALIFIER_DIRECTUS_HTTP_CLIENT = "directusHttpClient"

    // Pharmacy DI Qualifier Names
    const val DI_QUALIFIER_PHARMACY_JSON = "pharmacyJson"
    const val DI_QUALIFIER_PHARMACY_HTTP_CLIENT = "pharmacyHttpClient"

    // Data Store
    const val DATA_STORE_FILE_NAME = "prefs.preferences_pb"

    // Claims
    const val DEFAULT_FOLDER_ID = "f0767649-033d-403c-aa74-cb5d581ed898"
}
