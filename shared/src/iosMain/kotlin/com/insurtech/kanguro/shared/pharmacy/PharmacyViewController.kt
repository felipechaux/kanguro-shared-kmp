package com.insurtech.kanguro.shared.pharmacy

import androidx.compose.ui.window.ComposeUIViewController
import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyRequest
import com.insurtech.kanguro.shared.ui.composables.pharmacy.PharmacyView

fun PharmacyViewController(
    pharmacyRequest: PharmacyRequest,
    onNavigateBack: () -> Unit,
    onPharmacyViewOpened: () -> Unit = {},
    onPharmacyWebViewLoaded: () -> Unit = {},
    errorPharmacy: (String) -> Unit
) = ComposeUIViewController {
    PharmacyView(
        pharmacyRequest = pharmacyRequest,
        onNavigateBack = onNavigateBack,
        onPharmacyViewOpened = onPharmacyViewOpened,
        onPharmacyWebViewLoaded = onPharmacyWebViewLoaded,
        errorPharmacy = errorPharmacy
    )
}
