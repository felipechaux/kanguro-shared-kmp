package com.insurtech.kanguro.shared.data.petter

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class PetterService(
    private val client: HttpClient
) {
    suspend fun getPetterLink(request: PetterRequest): PetterResult {
        return client.post("/flows/trigger/2315a76b-f33d-4735-a12a-93809d657fb6") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<PetterResult>()
    }
}
