package com.insurtech.kanguro.shared.ui.composables.liveVet

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LiveVetViewModel : ViewModel() {

    private val _state: MutableStateFlow<LiveVetUiState> = MutableStateFlow(LiveVetUiState())
    val state: StateFlow<LiveVetUiState> get() = _state.asStateFlow()

    data class LiveVetUiState(
        val isLoading: Boolean = false,
        val isError: Boolean = false
    )
}
