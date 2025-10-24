package com.insurtech.kanguro.shared.data.claims.api

import com.insurtech.kanguro.shared.data.Result
import com.insurtech.kanguro.shared.data.claims.api.request.UpdateUserProfileRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody

class ProfileService(
    private val client: HttpClient
) {
    suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<Boolean> {
        return try {
            val response = client.put("/api/user/updateUserProfile") {
                header("Content-Type", "application/json")
                // Authorization header is automatically handled by TokenRefreshPlugin
                setBody(request)
            }
            val isSuccess = response.status.value in 200..299
            Result.Success(isSuccess)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
