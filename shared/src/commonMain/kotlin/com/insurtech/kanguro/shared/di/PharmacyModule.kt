package com.insurtech.kanguro.shared.di

import com.insurtech.kanguro.shared.Constants
import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyService
import com.insurtech.kanguro.shared.data.pharmacy.repository.PharmacyRepository
import com.insurtech.kanguro.shared.ui.composables.pharmacy.PharmacyViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val pharmacyModule = module {
    single(named(Constants.DI_QUALIFIER_PHARMACY_JSON)) {
        Json {
            coerceInputValues = true
            ignoreUnknownKeys = true
        }
    }

    single(named(Constants.DI_QUALIFIER_PHARMACY_HTTP_CLIENT)) {
        HttpClient {
            install(ContentNegotiation) {
                json(get(named(Constants.DI_QUALIFIER_PHARMACY_JSON)))
            }
            install(DefaultRequest) {
                url {
                    protocol = URLProtocol.HTTPS
                    host = getProperty(Constants.PROPERTY_QUEST_SYSTEM_HOST, "")
                }
            }
        }
    }

    single { PharmacyService(get(named(Constants.DI_QUALIFIER_PHARMACY_HTTP_CLIENT))) }
    single { PharmacyRepository(get()) }
    viewModel { PharmacyViewModel() }
}
