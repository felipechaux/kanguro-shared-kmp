package com.insurtech.kanguro.shared.data.payment

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class PaymentArrearsBannerEventService {
    
    private val _paymentMethodModalClosed = MutableSharedFlow<Unit>()
    val paymentMethodModalClosed: SharedFlow<Unit> = _paymentMethodModalClosed.asSharedFlow()

    suspend fun notifyPaymentMethodModalClosed() {
        _paymentMethodModalClosed.emit(Unit)
    }
}

// Global instance to avoid singleton object issues with SKIE
private val globalEventService = PaymentArrearsBannerEventService()

// Top-level function to avoid SKIE issues with companion objects
suspend fun notifyPaymentMethodModalClosed() {
    globalEventService.notifyPaymentMethodModalClosed()
}

// Top-level function to get the event service
fun getPaymentArrearsBannerEventService(): PaymentArrearsBannerEventService {
    return globalEventService
}