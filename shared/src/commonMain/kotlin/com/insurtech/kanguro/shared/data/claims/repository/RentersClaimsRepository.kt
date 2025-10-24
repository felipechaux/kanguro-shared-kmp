package com.insurtech.kanguro.shared.data.claims.repository

import com.insurtech.kanguro.shared.data.claims.api.RentersClaimsService
import com.insurtech.kanguro.shared.data.claims.api.UploadResponse
import com.insurtech.kanguro.shared.data.claims.api.request.RentersClaimRequest
import com.insurtech.kanguro.shared.data.claims.api.response.RentersClaimResponse

class RentersClaimsRepository(private val rentersClaimsService: RentersClaimsService) {

    suspend fun submitClaim(request: RentersClaimRequest): RentersClaimResponse {
        return rentersClaimsService.submitClaim(request)
    }

    suspend fun uploadAttachment(fileName: String, file: ByteArray): UploadResponse {
        return rentersClaimsService.uploadAttachment(
            fileName,
            file
        )
    }

    suspend fun getAllClaimsByIds(policiesIds: List<String>) = rentersClaimsService.getAllClaimsByIds(policiesIds)
}
