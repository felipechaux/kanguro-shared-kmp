package com.insurtech.kanguro.shared.domain.claims

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
actual data class StartDestination actual constructor(
    actual val destination: @Contextual @RawValue  Any
) : Parcelable
