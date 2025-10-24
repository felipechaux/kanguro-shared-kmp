package com.insurtech.kanguro.shared.di

import com.insurtech.kanguro.shared.Constants
import com.insurtech.kanguro.shared.data.claims.api.RentersClaimsService
import com.insurtech.kanguro.shared.data.claims.repository.RentersClaimsRepository
import com.insurtech.kanguro.shared.ui.composables.claims.screens.RentersClaimsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val claimsModule = module {
    single(named(Constants.DI_QUALIFIER_DIRECTUS_JSON)) {
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }

    single(named(Constants.DI_QUALIFIER_DIRECTUS_HTTP_CLIENT)) {
        HttpClient {
            install(ContentNegotiation) {
                json(get(named(Constants.DI_QUALIFIER_DIRECTUS_JSON)))
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = getProperty(Constants.PROPERTY_QUEST_SYSTEM_HOST, "")
                }
                header(Constants.HEADER_AUTHORIZATION, "${Constants.HEADER_BEARER_PREFIX} ${getProperty(Constants.PROPERTY_QUEST_SYSTEM_AUTH_TOKEN, "")}")
            }
        }
    }
    single<RentersClaimsService> { RentersClaimsService(get(named(Constants.DI_QUALIFIER_DIRECTUS_HTTP_CLIENT))) }
    single<RentersClaimsRepository> { RentersClaimsRepository(get()) }
    viewModel { RentersClaimsViewModel() }
}
