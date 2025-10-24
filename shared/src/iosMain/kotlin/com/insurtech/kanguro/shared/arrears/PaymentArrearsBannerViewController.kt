package com.insurtech.kanguro.shared.arrears

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsUiModel
import com.insurtech.kanguro.shared.ui.composables.PaymentArrearsBanner

fun PaymentArrearsBannerViewController(
    paymentArrears: PaymentArrearsUiModel,
    onViewed: () -> Unit = {},
    onClick: () -> Unit,
    shouldShowBanner: (shouldBeShown: Boolean) -> Unit
) = ComposeUIViewController {
    PaymentArrearsBanner(
        modifier = Modifier.padding(start = 20.dp, end = 40.dp, top = 20.dp),
        shape = RoundedCornerShape(0.dp),
        paymentArrears = paymentArrears,
        onViewed = onViewed,
        onClick = onClick,
        shouldShowBanner = shouldShowBanner
    )
}
