package com.insurtech.kanguro.shared.data.claims.api

import com.insurtech.kanguro.shared.Constants
import com.insurtech.kanguro.shared.data.claims.api.request.RentersClaimRequest
import com.insurtech.kanguro.shared.data.claims.api.response.RentersClaimResponse
import com.insurtech.kanguro.shared.data.claims.api.response.RentersClaimSubmitted
import com.insurtech.kanguro.shared.data.claims.api.response.RentersClaimsListResponse
import com.insurtech.kanguro.shared.utils.MimeTypeUtil
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class RentersClaimsService(
    private val client: HttpClient
) {
    suspend fun submitClaim(request: RentersClaimRequest): RentersClaimResponse {
        return client.post("/items/renters_claims") {
            header("Content-Type", "application/json")
            setBody(request)
        }.body<RentersClaimResponse>()
    }

    suspend fun uploadAttachment(fileName: String, fileBytes: ByteArray): UploadResponse {
        val response = client.submitFormWithBinaryData(
            url = "/files",
            formData = formData {
                append("folder", Constants.DEFAULT_FOLDER_ID)
                append(
                    "file",
                    fileBytes,
                    Headers.build {
                        append(HttpHeaders.ContentType, MimeTypeUtil.getMimeType(fileName))
                        append(HttpHeaders.ContentDisposition, "filename=$fileName")
                    }
                )
            }
        )
        println("Response status: ${response.status}")
        return response.body<UploadResponse>()
    }

    suspend fun getAllClaimsByIds(policiesIds: List<String>): List<RentersClaimSubmitted> {
        val policyIdsString = policiesIds.joinToString(",")
        val response = client.get("/items/renters_claims?filter[policy_id_ci][_in]=$policyIdsString&sort=-date_created") {
            header("Content-Type", "application/json")
        }.body<RentersClaimsListResponse>()
        return response.data
    }
}
