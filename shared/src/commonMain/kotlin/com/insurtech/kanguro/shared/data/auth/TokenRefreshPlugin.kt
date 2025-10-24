package com.insurtech.kanguro.shared.data.auth

import com.insurtech.kanguro.shared.Constants
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpClientPlugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.util.AttributeKey
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class TokenRefreshConfig {
    var sessionManager: SessionManager? = null
}

class TokenRefreshFeature(private val config: TokenRefreshConfig) {
    private val refreshMutex = Mutex()

    suspend fun ensureValidToken(): String? {
        val sessionManager = config.sessionManager ?: return null

        // Check if token is valid first
        if (sessionManager.isAccessTokenValid()) {
            return sessionManager.getAccessToken()
        }

        // Use mutex to prevent multiple concurrent refresh attempts
        return refreshMutex.withLock {
            // Double-check token validity after acquiring lock
            if (sessionManager.isAccessTokenValid()) {
                return@withLock sessionManager.getAccessToken()
            }

            // Attempt to refresh token
            val refreshSuccessful = sessionManager.refreshTokenIfNeeded()
            if (refreshSuccessful) {
                sessionManager.getAccessToken()
            } else {
                null
            }
        }
    }
}

object TokenRefreshPlugin : HttpClientPlugin<TokenRefreshConfig, TokenRefreshFeature> {
    override val key: AttributeKey<TokenRefreshFeature> = AttributeKey("TokenRefreshPlugin")

    override fun prepare(block: TokenRefreshConfig.() -> Unit): TokenRefreshFeature {
        val config = TokenRefreshConfig().apply(block)
        return TokenRefreshFeature(config)
    }

    override fun install(plugin: TokenRefreshFeature, scope: HttpClient) {
        scope.requestPipeline.intercept(HttpRequestPipeline.State) {
            // Ensure we have a valid token before proceeding
            val validToken = plugin.ensureValidToken()
            if (validToken != null) {
                // Add or update the Authorization header with the current valid token
                context.headers.remove(Constants.HEADER_AUTHORIZATION)
                context.headers.append(Constants.HEADER_AUTHORIZATION, "${Constants.HEADER_BEARER_PREFIX} $validToken")
            }
            proceed()
        }
    }
}