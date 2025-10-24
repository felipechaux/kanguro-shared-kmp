package com.insurtech.kanguro.shared.ui.composables

import androidx.lifecycle.viewModelScope
import com.insurtech.kanguro.shared.data.payment.getPaymentArrearsBannerEventService
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsBannerRepository
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class PaymentArrearsBannerVisibilityViewModel : ViewModel(), KoinComponent {
    private val repository: PaymentArrearsBannerRepository by inject()
    private val _paymentArrears = MutableStateFlow<PaymentArrearsUiModel?>(null)
    // Track whether we navigated to payment method from payment arrears banner
    private var _isTrackingPaymentMethodNavigation = false
    private var _isUpdatingFromModalClose = false
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        // Listen for payment method modal closed events
        viewModelScope.launch {
            getPaymentArrearsBannerEventService().paymentMethodModalClosed.collect {
                onPaymentMethodModalClosed()
            }
        }
    }

    fun updatePaymentArrears(
        paymentArrears: PaymentArrearsUiModel,
        cooldownDurationMillis: Long = PaymentArrearsBannerRepository.DEFAULT_COOLDOWN_DURATION_MILLIS
    ) {
        // Skip update if we're currently processing a modal close to avoid race conditions
        if (_isUpdatingFromModalClose) {
            return
        }

        _paymentArrears.value = paymentArrears

        viewModelScope.launch {
            val shouldShow = repository.shouldShowPaymentArrearsBanner(
                paymentArrears = paymentArrears,
                cooldownDurationMillis = cooldownDurationMillis
            )
            _state.value = _state.value.copy(shouldShowBanner = shouldShow)
        }
    }

    fun onBannerClickedNavigatingToPaymentMethod() {
        _isTrackingPaymentMethodNavigation = true
    }

    fun onPaymentMethodModalClosed() {
        if (_isTrackingPaymentMethodNavigation) {
            viewModelScope.launch {
                _isUpdatingFromModalClose = true
                
                val currentTimestamp = Clock.System.now().toEpochMilliseconds()
                repository.saveLastUpdateInArrearsPayment(currentTimestamp)

                // Update the visibility state after saving the timestamp
                _paymentArrears.value?.let { paymentArrears ->
                    val shouldShow = repository.shouldShowPaymentArrearsBanner(paymentArrears)
                    _state.value = _state.value.copy(shouldShowBanner = shouldShow)
                }

                // Reset tracking state
                _isTrackingPaymentMethodNavigation = false
                _isUpdatingFromModalClose = false
            }
        }
    }

    data class UiState(
        val shouldShowBanner: Boolean = false
    )

}