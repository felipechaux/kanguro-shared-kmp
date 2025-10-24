package com.insurtech.kanguro.shared.data.pharmacy.repository

import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyRequest
import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyResponse
import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyService

class PharmacyRepository(private val service: PharmacyService) {
    suspend fun getPharmacyLink(request: PharmacyRequest): PharmacyResponse {
        return service.getPharmacyLink(request)
    }
}
