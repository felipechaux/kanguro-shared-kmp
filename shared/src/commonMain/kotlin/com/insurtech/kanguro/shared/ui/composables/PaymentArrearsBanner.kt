package com.insurtech.kanguro.shared.ui.composables

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsBannerRepository.Companion.DEFAULT_COOLDOWN_DURATION_MILLIS
import com.insurtech.kanguro.shared.data.payment.PaymentArrearsUiModel
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.theme.MobaBodyRegular
import com.insurtech.kanguro.shared.ui.theme.MobaCaptionBold
import com.insurtech.kanguro.shared.ui.theme.MobaSubheadBold
import com.insurtech.kanguro.shared.ui.theme.NegativeDark
import com.insurtech.kanguro.shared.ui.theme.NegativeDarkest
import com.insurtech.kanguro.shared.ui.theme.PrimaryDark
import com.insurtech.kanguro.shared.ui.theme.PrimaryDarkest
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import com.insurtech.kanguro.shared.ui.theme.WarningDark
import com.insurtech.kanguro.shared.ui.theme.WarningDarkest
import com.insurtech.kanguro.shared.ui.theme.White
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PaymentArrearsBanner(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape,
    shadowElevation: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
    paymentArrears: PaymentArrearsUiModel,
    onViewed: () -> Unit = {},
    onClick: () -> Unit,
    shouldShowBanner: (shouldBeShown: Boolean) -> Unit = {}
) {
    val visibilityViewModel = koinViewModel<PaymentArrearsBannerVisibilityViewModel>()
    val state by visibilityViewModel.state.collectAsState()
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    LaunchedEffect(paymentArrears, DEFAULT_COOLDOWN_DURATION_MILLIS, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            visibilityViewModel.updatePaymentArrears(paymentArrears, DEFAULT_COOLDOWN_DURATION_MILLIS)
        }
    }

    LaunchedEffect(state.shouldShowBanner) {
        shouldShowBanner(state.shouldShowBanner)
    }

    if (state.shouldShowBanner) {
        PaymentArrearsBannerContent(
            modifier = modifier,
            shape = shape,
            shadowElevation = shadowElevation,
            contentPadding = contentPadding,
            paymentArrears = paymentArrears,
            onViewed = onViewed,
            onClick = {
                onClick()
                visibilityViewModel.onBannerClickedNavigatingToPaymentMethod()
            }
        )
    }
}


@Composable
private fun PaymentArrearsBannerContent(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape,
    shadowElevation: Dp = 0.dp,
    contentPadding: Dp = 0.dp,
    paymentArrears: PaymentArrearsUiModel,
    onViewed: () -> Unit,
    onClick: () -> Unit
) {

    val viewModel = remember { PaymentArrearsBannerViewModel() }

    LaunchedEffect(Unit) {
        onViewed()
    }

    // Define urgency levels based on days overdue
    val urgencyLevel = when {
        paymentArrears.daysInArrears <= 15 -> UrgencyLevel.WARNING
        paymentArrears.daysInArrears <= 30 -> UrgencyLevel.URGENT
        else -> UrgencyLevel.CRITICAL
    }

    // Color scheme based on urgency
    val colorScheme = getUrgencyColorScheme(urgencyLevel)

    // Animation for critical payments
    val infiniteTransition = rememberInfiniteTransition()
    val criticalPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (urgencyLevel == UrgencyLevel.CRITICAL) 0.7f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = shadowElevation,
                shape = shape
            )
            .alpha(criticalPulse)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = colorScheme.backgroundColor),
        shape = shape
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                colorScheme.backgroundColor,
                                colorScheme.backgroundColor.copy(alpha = 0.95f)
                            )
                        )
                    ).padding(contentPadding)
            ) {
                // Header with urgency indicator
                PaymentArrearsHeader(
                    daysInArrears = paymentArrears.daysInArrears,
                    urgencyLevel = urgencyLevel,
                    colorScheme = colorScheme
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Main payment content
                PaymentArrearsContent(
                    paymentArrears = paymentArrears,
                    viewModel = viewModel,
                    colorScheme = colorScheme
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Call-to-action button
                PaymentArrearsCTAButton(
                    colorScheme = colorScheme,
                    onClick = onClick
                )
            }
        }
    }
}

/**
 * Header section of the payment arrears banner showing urgency badge and warning icon
 */
@Composable
private fun PaymentArrearsHeader(
    daysInArrears: Int,
    urgencyLevel: UrgencyLevel,
    colorScheme: UrgencyColorScheme
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Urgency indicator badge
        Box(
            modifier = Modifier
                .background(
                    color = colorScheme.badgeColor,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "$daysInArrears " + stringResource(
                    if (daysInArrears == 1) MR.strings.day_overdue else MR.strings.days_overdue
                ),
                style = MobaCaptionBold(),
                color = colorScheme.badgeTextColor,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Warning icon for urgency levels
        Image(
            painter = painterResource(MR.images.ic_warning_arrear),
            contentDescription = urgencyLevel.label,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(colorScheme.urgencyLabelColor)
        )
    }
}

/**
 * Content section showing main message and amount due
 */
@Composable
private fun PaymentArrearsContent(
    paymentArrears: PaymentArrearsUiModel,
    viewModel: PaymentArrearsBannerViewModel,
    colorScheme: UrgencyColorScheme
) {
    // Main payment message
    Text(
        text = viewModel.getMainMessage(paymentArrears),
        style = MobaBodyRegular(),
        color = colorScheme.primaryTextColor,
        lineHeight = 20.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    // Amount due with emphasis
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = viewModel.getAmountMessage(paymentArrears),
            style = MobaSubheadBold(),
            color = colorScheme.urgencyLabelColor,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

/**
 * Call-to-action button for updating payment
 */
@Composable
private fun PaymentArrearsCTAButton(
    colorScheme: UrgencyColorScheme,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        colorScheme.urgencyLabelColor,
                        colorScheme.urgencyLabelColor
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(MR.images.ic_payment),
                contentDescription = null,
                modifier = Modifier.size(30.dp).padding(end = 8.dp),
                colorFilter = ColorFilter.tint(White)
            )

            Text(
                text = stringResource(MR.strings.click_here_to_update_payment),
                style = MobaSubheadBold(),
                color = White,
                textAlign = TextAlign.Center,
                fontSize = 14.sp,
                letterSpacing = 0.25.sp
            )

            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

/**
 * Represents different urgency levels for payment arrears
 */
private enum class UrgencyLevel(
    val label: String
) {
    WARNING("Warning"),
    URGENT("Urgent"),
    CRITICAL("Critical")
}

/**
 * Color scheme for different urgency levels
 */
private data class UrgencyColorScheme(
    val backgroundColor: Color,
    val primaryTextColor: Color,
    val amountColor: Color,
    val badgeColor: Color,
    val badgeTextColor: Color,
    val urgencyLabelColor: Color,
    val ctaButtonStartColor: Color,
    val ctaButtonEndColor: Color
)

/**
 * Gets the appropriate color scheme based on urgency level
 */
@Composable
private fun getUrgencyColorScheme(urgencyLevel: UrgencyLevel): UrgencyColorScheme {
    return when (urgencyLevel) {
        UrgencyLevel.WARNING -> UrgencyColorScheme(
            backgroundColor = White,
            primaryTextColor = SecondaryDarkest,
            amountColor = WarningDarkest,
            badgeColor = WarningDark,
            badgeTextColor = White,
            urgencyLabelColor = WarningDarkest,
            ctaButtonStartColor = WarningDarkest,
            ctaButtonEndColor = WarningDark
        )
        UrgencyLevel.URGENT -> UrgencyColorScheme(
            backgroundColor = White,
            primaryTextColor = SecondaryDarkest,
            amountColor = PrimaryDarkest,
            badgeColor = PrimaryDarkest,
            badgeTextColor = White,
            urgencyLabelColor = PrimaryDarkest,
            ctaButtonStartColor = PrimaryDarkest,
            ctaButtonEndColor = PrimaryDark
        )
        UrgencyLevel.CRITICAL -> UrgencyColorScheme(
            backgroundColor = White,
            primaryTextColor = SecondaryDarkest,
            amountColor = NegativeDarkest,
            badgeColor = NegativeDarkest,
            badgeTextColor = White,
            urgencyLabelColor = NegativeDarkest,
            ctaButtonStartColor = NegativeDarkest,
            ctaButtonEndColor = NegativeDark
        )
    }
}
