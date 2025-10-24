package com.insurtech.kanguro.shared.airvet

import androidx.compose.ui.window.ComposeUIViewController
import com.insurtech.kanguro.shared.ui.composables.liveVet.VetConnectionBottomSheetScreen

fun VetConnectionBottomSheetController(
    customerId: String,
    onOpenAirvetPressed: () -> Unit = {},
    onSignUpPressed: () -> Unit = {},
    onAirvetPressed: () -> Unit = {},
    onFindVetPressed: () -> Unit = {},
    onClosePressed: () -> Unit = {}
) =
    ComposeUIViewController {
        VetConnectionBottomSheetScreen(
            customerId = customerId,
            onOpenAirvetPressed = onOpenAirvetPressed,
            onSignUpPressed = onSignUpPressed,
            onAirvetPressed = onAirvetPressed,
            onFindVetPressed = onFindVetPressed,
            onClosePressed = onClosePressed
        )
    }
