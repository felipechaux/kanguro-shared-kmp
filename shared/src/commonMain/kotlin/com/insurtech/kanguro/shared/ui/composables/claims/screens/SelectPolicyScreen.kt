package com.insurtech.kanguro.shared.ui.composables.claims.screens

import ScreenScaffold
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.claims.model.ClaimRentersPolicy
import com.insurtech.kanguro.shared.ui.theme.*
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.serialization.Serializable

@Serializable
object SelectPolicyScreen

@Composable
fun SelectPolicyScreen(
    rentersPolicy: List<ClaimRentersPolicy>,
    onNext: () -> Unit,
    onClose: (() -> Unit)? = null,
    viewModel: RentersClaimsViewModel
) {
    val selectedPolicyId by viewModel.selectedPolicyId.collectAsState()

    ScreenScaffold(
        onNext = { if (selectedPolicyId != null) onNext() },
        onClose = onClose,
        showBack = false
    ) {
        if (rentersPolicy.size == 1) {
            LaunchedEffect(Unit) {
                viewModel.setShowCloseButtonNext(true)
                viewModel.setSelectedPolicy(rentersPolicy.first().id)
                onNext()
            }
        } else if (rentersPolicy.size > 1) {
            viewModel.setShowCloseButtonNext(false)
            Text(
                stringResource(MR.strings.select_policy_title),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = SecondaryDarkest,
                modifier = Modifier.padding(bottom = 8.dp, start = 16.dp, end = 16.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 24.dp, start = 16.dp, end = 16.dp),
                thickness = 2.dp,
                color = SecondaryDarkest
            )
            Text(
                stringResource(MR.strings.select_policy_subtitle),
                style = MobaBodyRegular(),
                color = SecondaryDarkest,
                modifier = Modifier.align(Alignment.Start).padding(start = 16.dp, end = 16.dp)
            )
            Spacer(Modifier.height(spacingNanoLarge))
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                items(rentersPolicy) { policy ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.setSelectedPolicy(policy.id) },
                        colors = if (selectedPolicyId == policy.id) {
                            CardDefaults.cardColors(containerColor = SecondaryDarkest.copy(alpha = 0.08f))
                        } else {
                            CardDefaults.cardColors(containerColor = NeutralBackground)
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(spacingXxs),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(policy.address, style = MobaBodyRegular(), color = SecondaryDarkest)
                                Text(stringResource(MR.strings.status_label) + " ${policy.status}", style = MobaSubheadRegular(), color = SecondaryMedium)
                            }
                            RadioButton(
                                selected = selectedPolicyId == policy.id,
                                onClick = { viewModel.setSelectedPolicy(policy.id) },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = SecondaryDarkest,
                                    unselectedColor = SecondaryLight
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
