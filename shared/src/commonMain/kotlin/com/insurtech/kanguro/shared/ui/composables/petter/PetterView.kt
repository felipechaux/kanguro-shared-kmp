package com.insurtech.kanguro.shared.ui.composables.petter

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.insurtech.kanguro.shared.Constants
import com.insurtech.kanguro.shared.data.petter.PetterRepository
import com.insurtech.kanguro.shared.data.petter.PetterRequest
import com.insurtech.kanguro.shared.data.petter.PetterService
import com.insurtech.kanguro.shared.data.questSystem.model.QuestStatus
import com.insurtech.kanguro.shared.data.questSystem.model.QuestValues
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.ScreenLoader
import com.insurtech.kanguro.shared.ui.composables.questSystem.QuestSystemViewModel
import com.insurtech.kanguro.shared.ui.theme.BKSHeading4
import com.insurtech.kanguro.shared.ui.theme.LatoBoldNeutralMedium2Size10
import com.insurtech.kanguro.shared.ui.theme.MobaBodyRegular
import com.insurtech.kanguro.shared.ui.theme.MobaSubheadBold
import com.insurtech.kanguro.shared.ui.theme.MobaTitle3
import com.insurtech.kanguro.shared.ui.theme.MuseoSans15RegularSecondaryDark
import com.insurtech.kanguro.shared.ui.theme.NeutralBackground
import com.insurtech.kanguro.shared.ui.theme.NeutralMedium
import com.insurtech.kanguro.shared.ui.theme.PrimaryDarkest
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import com.insurtech.kanguro.shared.ui.theme.TextButtonStyle
import com.insurtech.kanguro.shared.ui.theme.White
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

private const val BUTTON_STATE_JOIN = "JOIN"
private const val BUTTON_STATE_ACCOUNT = "ACCOUNT"

@Composable
fun rememberPetterRepository(
    petterHost: String
): PetterRepository = remember {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                }
            )
        }
        install(DefaultRequest) {
            url {
                protocol = URLProtocol.HTTPS
                host = petterHost
            }
        }
    }
    PetterRepository(PetterService(client))
}

@Composable
private fun PetterHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Image(
            painter = painterResource(MR.images.img_detail_petter),
            contentDescription = stringResource(MR.strings.petter_logo_content_description),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(14.dp))

        Image(
            painter = painterResource(MR.images.ic_petter),
            contentDescription = stringResource(MR.strings.petter_logo_content_description),
            modifier = Modifier.height(85.dp)
        )

        Text(
            text = stringResource(MR.strings.petter_main_heading),
            style = BKSHeading4(),
            color = PrimaryDarkest,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(MR.strings.petter_sub_heading),
            style = MuseoSans15RegularSecondaryDark(),
            color = SecondaryDarkest,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun DoubleBenefitsCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(MR.images.ic_coin),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(MR.strings.petter_double_benefits),
                    style = MobaSubheadBold(),
                    color = NeutralMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(MR.strings.petter_benefits_description),
                style = MobaBodyRegular(),
                color = SecondaryDarkest
            )
        }
    }
}

@Composable
private fun CashBackRewardsCard(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF5F5F5),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(48.dp),
                    painter = painterResource(MR.images.ic_star_coin),
                    contentDescription = null,
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(MR.strings.petter_cash_rewards),
                    style = MobaSubheadBold(),
                    color = NeutralMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(MR.strings.petter_rewards_description),
                style = MobaBodyRegular(),
                color = SecondaryDarkest
            )

            Spacer(modifier = Modifier.height(38.dp))
        }
    }
}

@Composable
private fun JoinNowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textButton: String
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryDarkest
        ),
        shape = RoundedCornerShape(28.dp)
    ) {
        Text(
            text = textButton,
            style = TextButtonStyle()
        )
    }
}

@Composable
private fun PetterTerms(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(MR.strings.petter_terms),
        style = LatoBoldNeutralMedium2Size10(),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun PetterCloseButton(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color.Black.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp)
    ) {
        IconButton(
            onClick = onNavigateBack
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(MR.strings.petter_close_content_description)
            )
        }
    }
}

@Composable
fun PetterView(
    petterRequest: PetterRequest,
    petterHost: String,
    customerId: String,
    onNavigateBack: () -> Unit,
    onPetterViewOpened: () -> Unit = {},
    onPetterWebViewLoaded: () -> Unit = {},
    openExternalLink: (String) -> Unit,
    errorPetter: (String) -> Unit
) {
    val questViewModel = koinViewModel<QuestSystemViewModel> { parametersOf(customerId, Constants.APP_LANGUAGE_EN) }
    val uiState by questViewModel.state.collectAsState()
    val questsState = uiState.quests
    var showWebView by remember { mutableStateOf(false) }
    var questProgressCreated by remember { mutableStateOf(false) }
    val repository = rememberPetterRepository(petterHost)
    val navigator =
        rememberWebViewNavigator(
            requestInterceptor =
            object : RequestInterceptor {
                override fun onInterceptUrlRequest(
                    request: WebRequest,
                    navigator: WebViewNavigator
                ): WebRequestInterceptResult {
                    return if (request.url.contains(Constants.PETTER_EXTERNAL_BROWSER)) {
                        val encoded =
                            request.url.substringAfter("${Constants.PETTER_EXTERNAL_BROWSER}?url=")
                        openExternalLink(encoded)
                        WebRequestInterceptResult.Reject
                    } else {
                        WebRequestInterceptResult.Allow
                    }
                }
            }
        )
    val viewModel = viewModel {
        PetterViewModel(petterRequest, repository)
    }

    LaunchedEffect(Unit) {
        onPetterViewOpened()
    }

    val petterQuest = questsState.find { quest ->
        quest.id == QuestValues.Petter.name
    }
    
    val buttonState = when {
        uiState.loading -> null
        petterQuest?.status == QuestStatus.COMPLETED || petterQuest?.status == QuestStatus.CLAIMED -> BUTTON_STATE_ACCOUNT
        petterQuest?.status == QuestStatus.PENDING -> BUTTON_STATE_JOIN
        else -> BUTTON_STATE_JOIN // Default to JOIN for new users
    }

    val petterButtonText = when (buttonState) {
        BUTTON_STATE_JOIN -> dev.icerock.moko.resources.compose.stringResource(MR.strings.petter_join_now)
        BUTTON_STATE_ACCOUNT -> dev.icerock.moko.resources.compose.stringResource(MR.strings.petter_go_to_my_account)
        else -> null
    }

    val shouldShowPetterButton = !uiState.loading && petterButtonText != null && buttonState != null

    Box(modifier = Modifier.fillMaxSize()) {
        if (viewModel.state.loading) {
            ScreenLoader(
                modifier = Modifier.fillMaxSize(),
                color = NeutralBackground
            )
        } else {
            if (showWebView) {
                val webViewState =
                    viewModel.state.petterURL?.let { rememberWebViewState(it) }
                if (webViewState != null) {
                    LaunchedEffect(showWebView) {
                        if (!questProgressCreated && petterQuest?.status != QuestStatus.COMPLETED && petterQuest?.status != QuestStatus.CLAIMED) {
                            questViewModel.createQuestProgress(QuestValues.Petter.name, QuestStatus.COMPLETED.name)
                            questProgressCreated = true
                        }
                    }
                    onPetterWebViewLoaded()
                    WebView(
                        state = webViewState,
                        navigator = navigator,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    errorPetter(stringResource(MR.strings.petter_error))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(MR.strings.petter_error),
                            style = MobaTitle3(),
                            color = SecondaryDarkest,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(start = 16.dp, end = 16.dp, bottom = 150.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PetterHeader(modifier = Modifier.padding(top = 16.dp))

                        Spacer(modifier = Modifier.height(24.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            DoubleBenefitsCard(modifier = Modifier.weight(1f))
                            CashBackRewardsCard(modifier = Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }

                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        color = White
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
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
                                        color = PrimaryDarkest
                                    )
                                }
                            } else if (shouldShowPetterButton) {
                                JoinNowButton(
                                    onClick = {
                                        viewModel.getPetterLink()
                                        showWebView = true
                                    },
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    textButton = petterButtonText,
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            PetterTerms(modifier = Modifier.padding(horizontal = 24.dp))

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        PetterCloseButton(
            onNavigateBack = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}
