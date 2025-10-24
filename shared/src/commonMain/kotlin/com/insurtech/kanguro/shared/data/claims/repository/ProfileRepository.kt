package com.insurtech.kanguro.shared.data.claims.repository

import com.insurtech.kanguro.shared.data.Result
import com.insurtech.kanguro.shared.data.claims.api.ProfileService
import com.insurtech.kanguro.shared.data.claims.api.request.UpdateUserProfileRequest

class ProfileRepository(private val profileService: ProfileService) {

    suspend fun updateUserProfile(
        givenName: String,
        surname: String,
        phone: String
    ): Result<Boolean> {
        val request = UpdateUserProfileRequest(
            givenName = givenName,
            surname = surname,
            phone = phone
        )
        return profileService.updateUserProfile(request)
    }
}
