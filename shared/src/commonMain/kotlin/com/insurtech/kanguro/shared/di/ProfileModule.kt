package com.insurtech.kanguro.shared.di

import com.insurtech.kanguro.shared.Constants
import com.insurtech.kanguro.shared.Constants.PROPERTY_KANGURO_ACCESS_TOKEN
import com.insurtech.kanguro.shared.Constants.PROPERTY_KANGURO_REFRESH_TOKEN
import com.insurtech.kanguro.shared.data.auth.AuthService
import com.insurtech.kanguro.shared.data.auth.DataStoreSessionManager
import com.insurtech.kanguro.shared.data.auth.DataStoreSessionRepository
import com.insurtech.kanguro.shared.data.auth.SessionManager
import com.insurtech.kanguro.shared.data.auth.SessionRepository
import com.insurtech.kanguro.shared.data.auth.TokenRefreshPlugin
import com.insurtech.kanguro.shared.data.claims.api.ProfileService
import com.insurtech.kanguro.shared.data.claims.repository.ProfileRepository
import com.insurtech.kanguro.shared.ui.composables.claims.screens.ClaimsProfileViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val profileModule = module {
    single(named(Constants.DI_QUALIFIER_PROFILE_JSON)) {
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }

    single<SessionRepository> {
        DataStoreSessionRepository(get())
    }

    single<SessionManager> {
        val sessionRepository = get<SessionRepository>()
        val sessionManager = DataStoreSessionManager(sessionRepository)
        val authService = get<AuthService>()
        sessionManager.setAuthService(authService)

        // Initialize with tokens from properties if available
        val initialAccessToken = getProperty(PROPERTY_KANGURO_ACCESS_TOKEN, "")
        val initialRefreshToken = getProperty(PROPERTY_KANGURO_REFRESH_TOKEN, "")

        if (initialAccessToken.isNotEmpty()) {
            runBlocking { sessionManager.setAccessToken(initialAccessToken) }
        }
        if (initialRefreshToken.isNotEmpty()) {
            runBlocking { sessionManager.setRefreshToken(initialRefreshToken) }
        }
        sessionManager
    }

    single(named(Constants.DI_QUALIFIER_AUTH_HTTP_CLIENT)) {
        HttpClient {
            install(ContentNegotiation) {
                json(get(named(Constants.DI_QUALIFIER_PROFILE_JSON)))
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = getProperty(Constants.PROPERTY_KANGURO_HOST, "")
                }
                header(Constants.HEADER_AUTHORIZATION, "${Constants.HEADER_BEARER_PREFIX} ${getProperty(Constants.PROPERTY_KANGURO_ACCESS_TOKEN, "")}")
                header(Constants.HEADER_API_KEY, getProperty(Constants.PROPERTY_KANGURO_API_KEY, ""))
            }
        }
    }

    single<AuthService> { AuthService(get(named(Constants.DI_QUALIFIER_AUTH_HTTP_CLIENT))) }

    single(named(Constants.DI_QUALIFIER_PROFILE_HTTP_CLIENT)) {
        val sessionManager: SessionManager = get()
        HttpClient {
            install(ContentNegotiation) {
                json(get(named(Constants.DI_QUALIFIER_PROFILE_JSON)))
            }
            install(TokenRefreshPlugin) {
                this.sessionManager = sessionManager
            }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = getProperty(Constants.PROPERTY_KANGURO_HOST, "")
                }
                header(Constants.HEADER_API_KEY, getProperty(Constants.PROPERTY_KANGURO_API_KEY, ""))
            }
        }
    }

    single<ProfileService> { ProfileService(get(named(Constants.DI_QUALIFIER_PROFILE_HTTP_CLIENT))) }
    single<ProfileRepository> { ProfileRepository(get()) }
    viewModel { ClaimsProfileViewModel() }
}
