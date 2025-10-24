package com.insurtech.kanguro.shared.data.petter

class PetterRepository(private val petterService: PetterService) {
    suspend fun getPetterLink(request: PetterRequest): PetterResult {
        return petterService.getPetterLink(request)
    }
}
