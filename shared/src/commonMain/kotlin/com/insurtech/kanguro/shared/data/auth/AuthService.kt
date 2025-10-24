package com.insurtech.kanguro.shared.data.auth

import com.insurtech.kanguro.shared.data.Result
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

class AuthService(private val client: HttpClient) {
    suspend fun refreshToken(refreshToken: String): Result<RefreshTokenResponse> {
        return try {
            val response = client.post("/api/user/refreshToken") {
                header("Content-Type", "application/json")
                setBody(RefreshTokenRequest(refreshToken))
            }
            if (response.status.value in 200..299) {
                val body = response.body<RefreshTokenResponse>()
                Result.Success(body)
            } else {
                Result.Error(Exception("Failed to refresh token: ${response.status}"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

@Serializable
data class RefreshTokenRequest(val refreshToken: String)

@Serializable
data class RefreshTokenResponse(
    val idToken: String,
    val accessToken: String,
    val refreshToken: String
)