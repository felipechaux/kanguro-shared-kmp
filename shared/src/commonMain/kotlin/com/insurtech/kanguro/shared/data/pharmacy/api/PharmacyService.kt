package com.insurtech.kanguro.shared.data.pharmacy.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PharmacyService(private val client: HttpClient) {
    suspend fun getPharmacyLink(request: PharmacyRequest): PharmacyResponse {
        return client.post("/flows/trigger/9c8bc323-ed2a-4ad9-99f8-ddea5513fec9") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PharmacyResponse>()
    }
}
