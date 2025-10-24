package com.insurtech.kanguro.shared.domain.claims

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
actual data class StartDestination actual constructor(
    actual val destination: @Contextual Any
)
