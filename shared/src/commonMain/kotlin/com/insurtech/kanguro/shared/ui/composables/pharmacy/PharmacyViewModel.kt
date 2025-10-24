package com.insurtech.kanguro.shared.ui.composables.pharmacy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyRequest
import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyResponse
import com.insurtech.kanguro.shared.data.pharmacy.repository.PharmacyRepository
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PharmacyViewModel() : ViewModel(), KoinComponent {
    private val repository: PharmacyRepository by inject()

    var state by mutableStateOf(UiState())

    fun fetchPharmacyLink(pharmacyRequest: PharmacyRequest) {
        try {
            viewModelScope.launch {
                state = state.copy(loading = true, error = null)
                val response: PharmacyResponse = repository.getPharmacyLink(pharmacyRequest)
                state = state.copy(pharmacyLink = response.link, loading = false)
            }
        } catch (e: Exception) {
            state = state.copy(
                loading = false,
                error = e.message
            )
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val pharmacyLink: String? = null,
        val error: String? = null
    )
}
