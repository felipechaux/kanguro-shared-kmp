package com.insurtech.kanguro.shared.airvet

import androidx.compose.ui.window.ComposeUIViewController
import com.insurtech.kanguro.shared.ui.composables.liveVet.SharedLiveVetBottomSheet

fun MainViewController(
    customerId: String,
    onEvent: () -> Unit = {},
    onClosePressed: () -> Unit = {}
) =
    ComposeUIViewController {
        SharedLiveVetBottomSheet(
            onEvent = onEvent,
            customerId = customerId,
            onClosePressed = onClosePressed
        )
    }
