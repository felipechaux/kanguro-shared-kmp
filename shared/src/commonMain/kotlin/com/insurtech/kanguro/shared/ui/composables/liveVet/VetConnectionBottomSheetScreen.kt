package com.insurtech.kanguro.shared.ui.composables.liveVet

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insurtech.kanguro.shared.Constants
import com.insurtech.kanguro.shared.data.questSystem.model.QuestStatus
import com.insurtech.kanguro.shared.data.questSystem.model.QuestValues
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.HeaderBackAndClose
import com.insurtech.kanguro.shared.ui.composables.KanguroButton
import com.insurtech.kanguro.shared.ui.composables.ScreenLoader
import com.insurtech.kanguro.shared.ui.composables.questSystem.QuestSystemViewModel
import com.insurtech.kanguro.shared.ui.theme.MobaBodyRegular
import com.insurtech.kanguro.shared.ui.theme.MobaTitle3
import com.insurtech.kanguro.shared.ui.theme.MobaTitle3SemiBold
import com.insurtech.kanguro.shared.ui.theme.NeutralBackground
import com.insurtech.kanguro.shared.ui.theme.PrimaryDarkest
import com.insurtech.kanguro.shared.ui.theme.PrimaryLight
import com.insurtech.kanguro.shared.ui.theme.PrimaryLightest
import com.insurtech.kanguro.shared.ui.theme.SecondaryDark
import com.insurtech.kanguro.shared.ui.theme.SecondaryLight
import com.insurtech.kanguro.shared.ui.theme.TertiaryDarkest
import com.insurtech.kanguro.shared.ui.theme.TertiaryLightest
import com.insurtech.kanguro.shared.ui.theme.White
import com.insurtech.kanguro.shared.ui.theme.spacingXxxs
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val BUTTON_STATE_OPEN = "OPEN"
private const val BUTTON_STATE_SIGNUP = "SIGNUP"

data class VetConnectionButtonCallbacks(
    val onOpenAirvetPressed: () -> Unit,
    val onSignUpPressed: () -> Unit,
    val onAirvetPressed: () -> Unit,
    val onFindVetPressed: () -> Unit
)

@Composable
fun VetConnectionBottomSheetScreen(
    modifier: Modifier = Modifier,
    customerId: String,
    onOpenAirvetPressed: () -> Unit = {},
    onSignUpPressed: () -> Unit = {},
    onAirvetPressed: () -> Unit = {},
    onFindVetPressed: () -> Unit = {},
    onClosePressed: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = spacingXxxs, start = spacingXxxs, end = spacingXxxs)
    ) {
        VetConnectionHeader(
            onClosePressed = { onClosePressed() }
        )

        VetConnectionContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            customerId = customerId,
            onOpenAirvetPressed = onOpenAirvetPressed,
            onSignUpPressed = onSignUpPressed,
            onAirvetPressed = onAirvetPressed,
            onFindVetPressed = onFindVetPressed
        )
    }
}

@Composable
private fun VetConnectionContent(
    modifier: Modifier = Modifier,
    customerId : String,
    onOpenAirvetPressed: () -> Unit = {},
    onSignUpPressed: () -> Unit = {},
    onAirvetPressed: () -> Unit,
    onFindVetPressed: () -> Unit
) {
    val viewModel = koinViewModel<QuestSystemViewModel> { parametersOf(customerId,  Constants.APP_LANGUAGE_EN) }
    val uiState by viewModel.state.collectAsState()
    val questsState = uiState.quests

    val airvetQuest = questsState.find { quest ->
        quest.id == QuestValues.Airvet.name 
    }

    val buttonState = when {
        uiState.loading -> null
        airvetQuest?.status == QuestStatus.COMPLETED || airvetQuest?.status == QuestStatus.CLAIMED -> BUTTON_STATE_OPEN
        airvetQuest?.status == QuestStatus.PENDING -> BUTTON_STATE_SIGNUP 
        else -> null
    }
    
    val airvetButtonText = when (buttonState) {
        BUTTON_STATE_OPEN -> dev.icerock.moko.resources.compose.stringResource(MR.strings.vet_connection_open_airvet)
        BUTTON_STATE_SIGNUP -> dev.icerock.moko.resources.compose.stringResource(MR.strings.vet_connection_sign_up_airvet)
        else -> null
    }

    val shouldShowAirvetButton = !uiState.loading && airvetButtonText != null

    Column(
        modifier = modifier.padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        VetConnectionMainCard()

        Spacer(modifier = Modifier.height(32.dp))

        VetConnectionButtons(
            uiState = uiState,
            airvetButtonText = airvetButtonText,
            buttonState = buttonState,
            shouldShowAirvetButton = shouldShowAirvetButton,
            callbacks = VetConnectionButtonCallbacks(
                onOpenAirvetPressed = onOpenAirvetPressed,
                onSignUpPressed = onSignUpPressed,
                onAirvetPressed = onAirvetPressed,
                onFindVetPressed = onFindVetPressed
            )
        )
    }
}

@Composable
private fun VetConnectionButtons(
    uiState: QuestSystemViewModel.UiState,
    airvetButtonText: String?,
    buttonState: String?,
    shouldShowAirvetButton: Boolean,
    callbacks: VetConnectionButtonCallbacks
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        White.copy(alpha = 0f),
                        White.copy(alpha = 0.8f),
                        White
                    )
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    ScreenLoader(
                        modifier = Modifier.fillMaxSize(),
                        color = TertiaryDarkest
                    )
                }
            } else if (shouldShowAirvetButton) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 6.dp
                    )
                ) {
                    KanguroButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        text = airvetButtonText!!,
                        enabled = true
                    ) {
                        callbacks.onAirvetPressed()
                        when (buttonState) {
                            BUTTON_STATE_OPEN -> callbacks.onOpenAirvetPressed()
                            BUTTON_STATE_SIGNUP -> callbacks.onSignUpPressed()
                        }
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                OutlinedButton(
                    onClick = callbacks.onFindVetPressed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TertiaryDarkest,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = dev.icerock.moko.resources.compose.stringResource(MR.strings.find_your_vet),
                            style = MobaBodyRegular().copy(
                                color = TertiaryDarkest,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun VetConnectionMainCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = NeutralBackground.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                TertiaryLightest,
                                PrimaryLightest
                            )
                        ),
                        shape = RoundedCornerShape(30.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = dev.icerock.moko.resources.compose.painterResource(MR.images.ic_vet_chat),
                    contentDescription = "Chat with vet",
                    colorFilter = ColorFilter.tint(TertiaryDarkest),
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(50.dp))
            
            Text(
                text = dev.icerock.moko.resources.compose.stringResource(MR.strings.vet_connection_main_message),
                style = MobaTitle3().copy(
                    fontSize = 20.sp,
                    lineHeight = 28.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = dev.icerock.moko.resources.compose.stringResource(MR.strings.vet_connection_continue_message),
                style = MobaBodyRegular().copy(
                    color = SecondaryLight,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun VetConnectionHeader(
    modifier: Modifier = Modifier,
    onClosePressed: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            TertiaryLightest.copy(alpha = 0.3f),
                            PrimaryLightest.copy(alpha = 0.2f),
                            White
                        )
                    )
                )
        )
        
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            HeaderBackAndClose(
                onClosePressed = onClosePressed
            )
            
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    VetConnectionTitleComponent()
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    VetConnectionSubtitleComponent()
                }
            }
        }
    }
}

@Composable
private fun VetConnectionTitleComponent(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    color = PrimaryLight.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = dev.icerock.moko.resources.compose.painterResource(MR.images.ic_medical_cross),
                contentDescription = "Medical care",
                colorFilter = ColorFilter.tint(PrimaryDarkest),
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.size(16.dp))
        
        Text(
            modifier = Modifier.weight(1f),
            text = dev.icerock.moko.resources.compose.stringResource(MR.strings.vet_connection_title),
            style = MobaTitle3SemiBold().copy(
                fontSize = 26.sp,
                color = PrimaryDarkest
            )
        )
    }
}

@Composable
private fun VetConnectionSubtitleComponent(
    modifier: Modifier = Modifier
) {
    Text(
        modifier = modifier,
        text = dev.icerock.moko.resources.compose.stringResource(MR.strings.vet_connection_subtitle),
        style = MobaTitle3().copy(
            fontSize = 16.sp,
            color = SecondaryDark,
            lineHeight = 22.sp
        )
    )
}

@Preview
@Composable
private fun VetConnectionBottomSheetPreview() {
    Surface {
        VetConnectionBottomSheetScreen(
            customerId = "",
            onAirvetPressed = {},
            onFindVetPressed = {},
            onClosePressed = {}
        )
    }
}