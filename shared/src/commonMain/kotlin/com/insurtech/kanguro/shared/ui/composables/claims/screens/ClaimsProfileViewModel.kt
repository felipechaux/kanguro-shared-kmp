package com.insurtech.kanguro.shared.ui.composables.claims.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insurtech.kanguro.shared.data.Result
import com.insurtech.kanguro.shared.data.claims.repository.ProfileRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ClaimsProfileViewModel() : ViewModel(), KoinComponent {

    private val profileRepository: ProfileRepository by inject()

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun updateUserProfile(givenName: String, surname: String, phone: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = profileRepository.updateUserProfile(givenName, surname, phone)

            when (result) {
                is Result.Success -> {
                    if (result.data) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Profile update failed."
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message ?: "Unknown error occurred"
                    )
                }
            }
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val error: String? = null
)
