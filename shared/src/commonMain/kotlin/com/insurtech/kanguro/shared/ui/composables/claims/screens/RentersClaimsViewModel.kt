package com.insurtech.kanguro.shared.ui.composables.claims.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insurtech.kanguro.shared.data.claims.api.request.RentersClaimRequest
import com.insurtech.kanguro.shared.data.claims.api.response.RentersClaimSubmitted
import com.insurtech.kanguro.shared.data.claims.repository.RentersClaimsRepository
import com.insurtech.kanguro.shared.ui.composables.claims.model.FileAttachment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RentersClaimsViewModel() : ViewModel(), KoinComponent {
    private val renterClaimRepository: RentersClaimsRepository by inject()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // --- Claim-in-progress state ---
    private val _selectedPolicyId = MutableStateFlow<String?>(null)
    val selectedPolicyId: StateFlow<String?> = _selectedPolicyId

    private val _incidentDescription = MutableStateFlow<String?>(null)
    val incidentDescription: StateFlow<String?> = _incidentDescription

    private val _incidentDate = MutableStateFlow<String?>(null)
    val incidentDate: StateFlow<String?> = _incidentDate

    private val _attachments = MutableStateFlow<List<FileAttachment>>(emptyList())
    val attachments: StateFlow<List<FileAttachment>> = _attachments

    private val _uploadedDocument = MutableStateFlow<List<String>>(emptyList()) // Now stores a list of IDs
    private val _phoneNumber = MutableStateFlow("")

    private val _claimSubmitted = MutableStateFlow(false)
    val claimSubmitted: StateFlow<Boolean> = _claimSubmitted

    private val _claimsList = MutableStateFlow<List<RentersClaimSubmitted>>(emptyList())
    val claimsList: StateFlow<List<RentersClaimSubmitted>> = _claimsList

    private val _showCloseButtonNext = MutableStateFlow(false)
    val showCloseButtonNext: StateFlow<Boolean> = _showCloseButtonNext

    private val _policiesList = MutableStateFlow<List<String>>(emptyList())

    fun setSelectedPolicy(id: String) { _selectedPolicyId.value = id }
    fun setIncidentDescription(desc: String) { _incidentDescription.value = desc }
    fun setIncidentDate(date: String) { _incidentDate.value = date }
    fun setPoliciesList(policies: List<String>) { _policiesList.value = policies }
    fun setPhoneNumber(phone: String) { _phoneNumber.value = phone }

    fun addAttachment(attachment: FileAttachment) {
        _attachments.update { it + attachment }
    }

    fun setShowCloseButtonNext(show: Boolean) {
        _showCloseButtonNext.value = show
    }

    fun resetClaimState() {
        _incidentDescription.value = null
        _incidentDate.value = null
        _attachments.value = emptyList()
        _uploadedDocument.value = emptyList()
        _phoneNumber.value = ""
        _error.value = null
        _isLoading.value = false
        _showCloseButtonNext.value = false
    }

    fun resetClaimSubmittedState() {
        _claimSubmitted.value = false
    }

    fun addDocumentId(documentId: String) {
        _uploadedDocument.update { it + documentId }
    }

    fun removeAttachment(attachment: FileAttachment) {
        _attachments.update { it - attachment }
    }

    fun submitClaim() {
        val policyId = selectedPolicyId.value
        val description = incidentDescription.value
        val date = incidentDate.value
        val uploadedDocumentIds = _uploadedDocument.value

        if (policyId.isNullOrBlank() || description.isNullOrBlank() || date.isNullOrBlank()) {
            return
        }
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    renterClaimRepository.submitClaim(
                        RentersClaimRequest(
                            customerDescription = description,
                            dateOfLoss = date,
                            policyIdCi = policyId,
                            latestPhoneNumber = _phoneNumber.value,
                            attachments = RentersClaimRequest.Attachments(
                                create = uploadedDocumentIds.map { id ->
                                    RentersClaimRequest.Create(directusFilesId = id)
                                }
                            )
                        )
                    )
                }
                _claimSubmitted.value = true
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
                _claimSubmitted.value = false
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun uploadDocument() {
        _isLoading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val responses = withContext(Dispatchers.IO) {
                    val uploadTasks = _attachments.value.map { attachment ->
                        async {
                            renterClaimRepository.uploadAttachment(
                                fileName = attachment.fileName,
                                file = attachment.fileByteArray
                            )
                        }
                    }
                    uploadTasks.awaitAll()
                }
                responses.forEach { response ->
                    addDocumentId(response.data.id)
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                submitClaim()
            }
        }
    }

    fun fetchAllClaimsByIds() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                val claims = withContext(Dispatchers.IO) {
                    renterClaimRepository.getAllClaimsByIds(policiesIds = _policiesList.value)
                }
                _claimsList.value = claims
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
