package com.insurtech.kanguro.shared.petter

import androidx.compose.ui.window.ComposeUIViewController
import com.insurtech.kanguro.shared.data.petter.PetterRequest
import com.insurtech.kanguro.shared.ui.composables.petter.PetterView

fun PetterViewController(
    petterRequest: PetterRequest,
    petterHost: String,
    customerId: String,
    onNavigateBack: () -> Unit,
    onPetterViewOpened: () -> Unit = {},
    onPetterWebViewLoaded: () -> Unit = {},
    openExternalLink: (String) -> Unit,
    errorPetter: (String) -> Unit
) = ComposeUIViewController {
    PetterView(
        petterRequest = petterRequest,
        petterHost = petterHost,
        customerId = customerId,
        onNavigateBack = onNavigateBack,
        onPetterViewOpened = onPetterViewOpened,
        onPetterWebViewLoaded = onPetterWebViewLoaded,
        openExternalLink = openExternalLink,
        errorPetter = errorPetter
    )
}
