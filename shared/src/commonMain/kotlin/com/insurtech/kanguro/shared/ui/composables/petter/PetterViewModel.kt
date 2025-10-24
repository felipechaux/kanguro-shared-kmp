package com.insurtech.kanguro.shared.ui.composables.petter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insurtech.kanguro.shared.data.petter.PetterRepository
import com.insurtech.kanguro.shared.data.petter.PetterRequest
import kotlinx.coroutines.launch

class PetterViewModel(
    private val petterRequest: PetterRequest,
    private val petterRepository: PetterRepository
) : ViewModel() {
    var state by mutableStateOf(UiState())

    fun getPetterLink() {
        viewModelScope.launch {
            try {
                state = state.copy(loading = true, error = null)
                val petterResponse = petterRepository.getPetterLink(petterRequest)
                state = state.copy(petterURL = petterResponse.link, loading = false)
            } catch (e: Exception) {
                state = state.copy(
                    loading = false
                )
            }
        }
    }

    data class UiState(
        val loading: Boolean = false,
        val petterURL: String? = null,
        val error: String? = null
    )
}
