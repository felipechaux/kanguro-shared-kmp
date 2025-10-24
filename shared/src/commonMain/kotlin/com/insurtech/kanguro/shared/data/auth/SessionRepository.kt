package com.insurtech.kanguro.shared.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.long
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

interface SessionRepository {
    suspend fun saveAccessToken(token: String?)
    suspend fun getAccessToken(): String?
    suspend fun saveRefreshToken(token: String?)
    suspend fun getRefreshToken(): String?
    suspend fun clearSession()
    suspend fun isAccessTokenValid(): Boolean

    // Flow versions for reactive access
    fun getAccessTokenFlow(): Flow<String?>
    fun getRefreshTokenFlow(): Flow<String?>
}

class DataStoreSessionRepository(
    private val dataStore: DataStore<Preferences>
) : SessionRepository {

    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    override suspend fun saveAccessToken(token: String?) {
        dataStore.edit { preferences ->
            if (token != null) {
                preferences[ACCESS_TOKEN_KEY] = token
            } else {
                preferences.remove(ACCESS_TOKEN_KEY)
            }
        }
    }

    override suspend fun getAccessToken(): String? {
        return dataStore.data.first()[ACCESS_TOKEN_KEY]
    }

    override suspend fun saveRefreshToken(token: String?) {
        dataStore.edit { preferences ->
            if (token != null) {
                preferences[REFRESH_TOKEN_KEY] = token
            } else {
                preferences.remove(REFRESH_TOKEN_KEY)
            }
        }
    }

    override suspend fun getRefreshToken(): String? {
        return dataStore.data.first()[REFRESH_TOKEN_KEY]
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(ACCESS_TOKEN_KEY)
            preferences.remove(REFRESH_TOKEN_KEY)
        }
    }

    override fun getAccessTokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN_KEY]
        }
    }

    override fun getRefreshTokenFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN_KEY]
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    override suspend fun isAccessTokenValid(): Boolean {
        return try {
            val token = getAccessToken()
            if (token.isNullOrBlank()) {
                return false
            }
            val parts = token.split(".")
            if (parts.size != 3) {
                return false
            }
            val payload = parts[1]
            val paddedPayload = when (payload.length % 4) {
                2 -> "$payload=="
                3 -> "$payload="
                else -> payload
            }
            val decodedBytes = Base64.decode(paddedPayload)
            val payloadJson = decodedBytes.decodeToString()
            val jsonObject = Json.parseToJsonElement(payloadJson) as JsonObject

            val exp = jsonObject["exp"]?.jsonPrimitive?.long
                ?: return false // Token must have exp claim

            val currentTimeSeconds = Clock.System.now().epochSeconds

            val leewaySeconds = 10L

            (currentTimeSeconds + leewaySeconds) < exp
        } catch (_: Exception) {
            false
        }
    }
}
