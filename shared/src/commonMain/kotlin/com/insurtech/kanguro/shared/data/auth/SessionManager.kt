package com.insurtech.kanguro.shared.data.auth

import com.insurtech.kanguro.shared.data.Result

interface SessionManager {
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun setAccessToken(token: String?)
    suspend fun setRefreshToken(token: String?)
    suspend fun isAccessTokenValid(): Boolean
    suspend fun refreshTokenIfNeeded(): Boolean
    suspend fun clearSession()
}

class DataStoreSessionManager(
    private val sessionRepository: SessionRepository,
    private var authService: AuthService? = null
) : SessionManager {

    fun setAuthService(authService: AuthService) {
        this.authService = authService
    }

    override suspend fun getAccessToken(): String? {
        return sessionRepository.getAccessToken()
    }

    override suspend fun getRefreshToken(): String? {
        return sessionRepository.getRefreshToken()
    }

    override suspend fun setAccessToken(token: String?) {
        sessionRepository.saveAccessToken(token)
    }

    override suspend fun setRefreshToken(token: String?) {
        sessionRepository.saveRefreshToken(token)
    }

    override suspend fun isAccessTokenValid(): Boolean {
        return sessionRepository.isAccessTokenValid()
    }

    override suspend fun refreshTokenIfNeeded(): Boolean {
        // Check if access token is valid first
        if (isAccessTokenValid()) {
            return true // Token is still valid, no refresh needed
        }

        // Check if we have tokens to refresh
        val currentAccessToken = getAccessToken()
        if (currentAccessToken.isNullOrBlank()) {
            return false // No token to refresh
        }

        val currentRefreshToken = getRefreshToken() ?: return false
        val authSvc = authService ?: return false

        return when (val result = authSvc.refreshToken(currentRefreshToken)) {
            is Result.Success -> {
                // Update tokens with the new values and persist them
                setAccessToken(result.data.accessToken)
                setRefreshToken(result.data.refreshToken)
                true // Successfully refreshed
            }
            is Result.Error -> {
                // Clear tokens on refresh failure
                clearSession()
                false // Failed to refresh
            }
        }
    }

    override suspend fun clearSession() {
        sessionRepository.clearSession()
    }
}
