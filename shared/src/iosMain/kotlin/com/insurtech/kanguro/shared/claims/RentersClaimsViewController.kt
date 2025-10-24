package com.insurtech.kanguro.shared.claims

import RentersClaimsNavHost
import androidx.compose.ui.window.ComposeUIViewController
import com.insurtech.kanguro.shared.domain.claims.StartDestination
import com.insurtech.kanguro.shared.ui.composables.claims.model.ClaimRentersPolicy
import com.insurtech.kanguro.shared.ui.composables.claims.model.UseProfile

fun RentersClaimsViewController(
    rentersPolicy: List<ClaimRentersPolicy>,
    startDestination: StartDestination,
    userProfile: UseProfile,
    phoneUpdated: (phone: String) -> Unit = {},
    onClose: () -> Unit
) = ComposeUIViewController {
    RentersClaimsNavHost(
        startDestination = startDestination,
        rentersPolicy = rentersPolicy,
        userProfile = userProfile,
        phoneUpdated = phoneUpdated,
        onClose = onClose
    )
}
