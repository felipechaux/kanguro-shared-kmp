package com.insurtech.kanguro.shared.ui.composables.claims.screens

import ScreenScaffold
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.KanguroButton
import com.insurtech.kanguro.shared.ui.composables.KanguroTextField
import com.insurtech.kanguro.shared.ui.composables.claims.model.UseProfile
import com.insurtech.kanguro.shared.ui.theme.MobaSubheadBold
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import com.insurtech.kanguro.shared.utils.isValidUSPhone
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object ContactInfoScreen

@Composable
fun ContactInfoScreen(
    viewModel: RentersClaimsViewModel,
    useProfile: UseProfile,
    phoneUpdated: (phone: String) -> Unit = {},
    onNext: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val claimsProfileViewModel = koinViewModel<ClaimsProfileViewModel>()
    val profileUiState by claimsProfileViewModel.uiState.collectAsState()
    val (phone, setPhone) = remember { mutableStateOf(useProfile.phone) }
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val claimSubmitted by viewModel.claimSubmitted.collectAsState()

    val isPhoneValid by remember(phone) {
        derivedStateOf {
            isValidUSPhone(phone)
        }
    }

    val phoneErrorMessage = if (phone.isBlank() || isPhoneValid) null else stringResource(MR.strings.invalid_us_phone_error)

    ScreenScaffold(
        onBack = onBack,
        nextLabel = "",
        showBack = true
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (claimSubmitted) {
                ContactInfoClaimSubmitted(
                    onNext = onNext,
                    viewModel = viewModel,
                    useProfile = useProfile,
                    phone = phone,
                    claimsProfileViewModel = claimsProfileViewModel,
                    phoneUpdated = phoneUpdated
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter),
                        verticalArrangement = Arrangement.Top
                    ) {
                        ContactInfoFields(
                            useProfile = useProfile,
                            phone = phone,
                            setPhone = setPhone,
                            phoneError = phoneErrorMessage,
                            error = error
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) {
                        ContactInfoSubmitRow(
                            isLoading = isLoading,
                            isPhoneValid = isPhoneValid,
                            phoneError = phoneErrorMessage,
                            onSubmit = {
                                viewModel.setPhoneNumber(phone)
                                viewModel.uploadDocument()
                            }
                        )
                    }
                }
                if (isLoading || profileUiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = SecondaryDarkest,
                            strokeWidth = 4.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactInfoClaimSubmitted(
    onNext: () -> Unit,
    viewModel: RentersClaimsViewModel,
    useProfile: UseProfile,
    phone: String,
    claimsProfileViewModel: ClaimsProfileViewModel,
    phoneUpdated: (phone: String) -> Unit = {}
) {
    LaunchedEffect(Unit) {
        onNext()
        viewModel.resetClaimState()
        if (useProfile.phone != phone) {
            claimsProfileViewModel.updateUserProfile(
                givenName = useProfile.givenName,
                surname = useProfile.surname,
                phone = phone
            )
            phoneUpdated(phone)
        }
    }
}

@Composable
private fun ContactInfoFields(
    useProfile: UseProfile,
    phone: String,
    setPhone: (String) -> Unit,
    phoneError: String?,
    error: String?
) {
    Text(
        stringResource(MR.strings.contact_info_title),
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        color = SecondaryDarkest,
        modifier = Modifier.padding(bottom = 4.dp)
    )
    HorizontalDivider(
        modifier = Modifier.padding(bottom = 24.dp),
        thickness = 2.dp,
        color = SecondaryDarkest
    )
    Text(
        text = stringResource(MR.strings.email_address_label),
        style = MobaSubheadBold(),
        color = SecondaryDarkest,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    KanguroTextField(
        value = useProfile.email,
        onValueChange = {},
        placeholder = stringResource(MR.strings.enter_email_placeholder),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        enabled = false
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = stringResource(MR.strings.phone_number_label),
        style = MobaSubheadBold(),
        color = SecondaryDarkest,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    KanguroTextField(
        value = phone,
        onValueChange = { input -> setPhone(input.filter { it.isDigit() }.take(12)) },
        placeholder = stringResource(MR.strings.enter_phone_placeholder),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = phoneError != null,
        errorMessage = phoneError,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
    if (error != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(MR.strings.error_label) + error, color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ContactInfoSubmitRow(
    isLoading: Boolean,
    isPhoneValid: Boolean,
    phoneError: String?,
    onSubmit: () -> Unit
) {
    Column {
        HorizontalDivider(
            modifier = Modifier.padding(bottom = 24.dp),
            thickness = 2.dp,
            color = SecondaryDarkest
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            KanguroButton(
                text = stringResource(MR.strings.submit_claim_button),
                onClick = onSubmit,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                enabled = !isLoading && phoneError == null && isPhoneValid
            )
        }
    }
}
