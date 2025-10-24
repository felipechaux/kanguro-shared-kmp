package com.insurtech.kanguro.shared.domain.claims

import androidx.compose.ui.graphics.Color
import com.insurtech.kanguro.shared.ui.theme.InformationDark
import com.insurtech.kanguro.shared.ui.theme.InformationLightest
import com.insurtech.kanguro.shared.ui.theme.NegativeDark
import com.insurtech.kanguro.shared.ui.theme.NegativeLightest
import com.insurtech.kanguro.shared.ui.theme.PositiveDark
import com.insurtech.kanguro.shared.ui.theme.PositiveLightest
import com.insurtech.kanguro.shared.ui.theme.WarningDark
import com.insurtech.kanguro.shared.ui.theme.WarningLightest

/**
 * Enum representing the various states a claim can be in throughout its lifecycle.
 */
enum class RentersClaimStatus(
    val displayText: String,
    val statusBarColor: Color,
    val badgeColors: Triple<Color, Color, Color> // backgroundColor, textColor, borderColor
) {
    SUBMITTED(
        displayText = "Submitted",
        statusBarColor = InformationDark,
        badgeColors = Triple(InformationLightest, InformationDark, InformationDark)
    ),

    IN_REVIEW(
        displayText = "In Review",
        statusBarColor = WarningDark,
        badgeColors = Triple(WarningLightest, WarningDark, WarningDark)
    ),

    CLOSED(
        displayText = "Closed",
        statusBarColor = PositiveDark,
        badgeColors = Triple(PositiveLightest, PositiveDark, PositiveDark)
    ),

    DENIED(
        displayText = "Denied",
        statusBarColor = NegativeDark,
        badgeColors = Triple(NegativeLightest, NegativeDark, NegativeDark)
    ),

    PAID(
        displayText = "Paid",
        statusBarColor = PositiveDark,
        badgeColors = Triple(PositiveLightest, PositiveDark, PositiveDark)
    );

    companion object {
        /**
         * Parses a string status value from the API and returns the corresponding ClaimStatus enum.
         * This method handles various formats including snake_case and camelCase variants.
         * * @param status The status string from the API
         * @return The corresponding ClaimStatus enum, or throws IllegalArgumentException if not recognized
         */
        fun fromString(status: String): RentersClaimStatus {
            return when (status.lowercase()) {
                "submitted" -> SUBMITTED
                "inreview" -> IN_REVIEW
                "closed" -> CLOSED
                "denied" -> DENIED
                "paid" -> PAID
                else -> throw IllegalArgumentException("Unknown status: $status")
            }
        }
    }
}
