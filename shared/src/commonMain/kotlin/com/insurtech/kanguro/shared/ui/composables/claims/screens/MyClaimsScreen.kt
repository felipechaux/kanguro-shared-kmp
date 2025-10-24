package com.insurtech.kanguro.shared.ui.composables.claims.screens

import ScreenScaffold
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.insurtech.kanguro.shared.data.claims.api.response.RentersClaimSubmitted
import com.insurtech.kanguro.shared.domain.claims.RentersClaimStatus
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.CustomToast
import com.insurtech.kanguro.shared.ui.theme.MobaBodyBold
import com.insurtech.kanguro.shared.ui.theme.MobaBodyRegular
import com.insurtech.kanguro.shared.ui.theme.MobaCaptionBold
import com.insurtech.kanguro.shared.ui.theme.MobaFootnoteBlack
import com.insurtech.kanguro.shared.ui.theme.MobaFootnoteRegular
import com.insurtech.kanguro.shared.ui.theme.MobaTitle3
import com.insurtech.kanguro.shared.ui.theme.NegativeDarkest
import com.insurtech.kanguro.shared.ui.theme.SecondaryDark
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import com.insurtech.kanguro.shared.ui.theme.White
import com.insurtech.kanguro.shared.ui.theme.spacingNanoLarge
import com.insurtech.kanguro.shared.ui.theme.spacingXxs
import com.insurtech.kanguro.shared.ui.theme.spacingXxxs
import com.insurtech.kanguro.shared.utils.DateUtils
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.serialization.Serializable

@Serializable
object MyClaimsScreen

@Composable
fun MyClaimsScreen(
    viewModel: RentersClaimsViewModel,
    onCreateNewClaim: () -> Unit
) {
    val error by viewModel.error.collectAsState()
    val claims by viewModel.claimsList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val claimSubmitted by viewModel.claimSubmitted.collectAsState()
    val (showToast, setShowToast) = remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(claimSubmitted) {
        if (claimSubmitted) {
            setShowToast(true)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAllClaimsByIds()
    }

    ScreenScaffold(
        onBack = null,
        nextLabel = "",
        showBack = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(spacingXxs * 2))

                Text(
                    text = stringResource(MR.strings.my_claims_title),
                    style = MobaTitle3().copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = SecondaryDarkest
                    ),
                    modifier = Modifier.padding(bottom = spacingXxxs + spacingXxs * 2)
                )

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = SecondaryDarkest,
                                strokeWidth = 4.dp
                            )
                        }
                    }
                    error != null -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(MR.strings.error_label, error!!),
                                color = NegativeDarkest,
                                style = MobaBodyRegular()
                            )
                        }
                    }
                    claims.isEmpty() -> {
                        EmptyClaimsState(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(spacingXxxs + spacingXxs * 2)
                        )
                    }
                    else -> {
                        ClaimsList(
                            modifier = Modifier.weight(1f),
                            claims = claims,
                            listState = listState
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = onCreateNewClaim,
                containerColor = SecondaryDarkest,
                contentColor = White,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(spacingXxxs)
                    .size(56.dp),
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(MR.strings.create_new_claim_button),
                    modifier = Modifier.size(24.dp)
                )
            }
            if (showToast) {
                CustomToast(
                    message = stringResource(MR.strings.claim_submitted_success),
                    isVisible = showToast,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .zIndex(Float.MAX_VALUE)
                        .padding(bottom = 20.dp),
                    onDismiss = {
                        viewModel.resetClaimSubmittedState()
                        setShowToast(false)
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyClaimsState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(MR.strings.no_claims_found),
            style = MobaBodyBold(),
            color = SecondaryDark
        )
    }
}

@Composable
private fun ClaimsList(
    modifier: Modifier = Modifier,
    claims: List<RentersClaimSubmitted>,
    listState: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacingNanoLarge),
        contentPadding = PaddingValues(top = spacingXxxs, bottom = 80.dp), // Bottom padding for FAB
        userScrollEnabled = true // Explicitly enable user scrolling
    ) {
        items(
            items = claims,
            key = { claim -> claim.id }
        ) { claim ->
            ClaimCard(
                claim = claim,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ClaimCard(
    modifier: Modifier = Modifier,
    claim: RentersClaimSubmitted
) {
    val claimStatus = RentersClaimStatus.fromString(claim.status)

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            // Status indicator bar on the left
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(
                        color = claimStatus.statusBarColor,
                        shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
                    )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(spacingXxxs)
            ) {
                // Header with policy info
                Text(
                    text = "${stringResource(MR.strings.policy_label)} ${claim.policyIdCi}",
                    style = MobaBodyBold(),
                    color = SecondaryDarkest
                )

                Spacer(modifier = Modifier.height(spacingNanoLarge))

                // Status badge
                ClaimStatusBadge(status = claimStatus)

                Spacer(modifier = Modifier.height(spacingNanoLarge))

                // Claim details
                ClaimInfoRow(
                    label = stringResource(MR.strings.claim_code),
                    value = claim.code ?: "N/A"
                )

                ClaimInfoRow(
                    label = stringResource(MR.strings.incident_date_label),
                    value = claim.dateOfLoss?.let { DateUtils.formatIsoToYyyyMmDd(it) } ?: "N/A"
                )

                ClaimInfoRow(
                    label = stringResource(MR.strings.date_created_label),
                    value = claim.dateCreated?.let { DateUtils.formatIsoToYyyyMmDd(it) } ?: "N/A"
                )
            }
        }
    }
}

@Composable
private fun ClaimStatusBadge(
    status: RentersClaimStatus
) {
    val (backgroundColor, textColor, borderColor) = status.badgeColors

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(2.dp, borderColor),
        shadowElevation = 0.dp
    ) {
        Text(
            text = status.displayText,
            style = MobaCaptionBold(),
            color = textColor,
            modifier = Modifier.padding(horizontal = spacingXxs + 4.dp, vertical = spacingXxs + 2.dp)
        )
    }
}

@Composable
private fun ClaimInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacingXxs / 2),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MobaFootnoteRegular(),
            color = SecondaryDark
        )
        Text(
            text = value,
            style = MobaFootnoteBlack(),
            color = SecondaryDark
        )
    }
}
