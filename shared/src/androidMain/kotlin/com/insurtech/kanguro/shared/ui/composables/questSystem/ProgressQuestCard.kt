package com.insurtech.kanguro.shared.ui.composables.questSystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.insurtech.kanguro.shared.Constants
import com.insurtech.kanguro.shared.Constants.APP_LANGUAGE_ES
import com.insurtech.kanguro.shared.data.questSystem.model.Quest
import com.insurtech.kanguro.shared.data.questSystem.model.QuestStatus
import com.insurtech.kanguro.shared.data.questSystem.model.QuestValues
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.ConfettiAnimation
import com.insurtech.kanguro.shared.ui.composables.KanguroButton
import com.insurtech.kanguro.shared.ui.composables.ScreenLoader
import com.insurtech.kanguro.shared.ui.theme.MobaBodyRegular
import com.insurtech.kanguro.shared.ui.theme.MobaTitle1
import com.insurtech.kanguro.shared.ui.theme.MobaTitle3
import com.insurtech.kanguro.shared.ui.theme.PrimaryDarkest
import com.insurtech.kanguro.shared.ui.theme.PrimaryLight
import com.insurtech.kanguro.shared.utils.PostHogUtils
import com.insurtech.kanguro.shared.utils.PostHogUtils.POSTHOG_AIRVET_FLAG
import com.insurtech.kanguro.shared.utils.PostHogUtils.POSTHOG_MEDICAL_HISTORY_FLAG
import com.insurtech.kanguro.shared.utils.PostHogUtils.POSTHOG_PETTER_FLAG
import com.insurtech.kanguro.shared.utils.QuestFilterUtils
import com.posthog.PostHog
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Locale

fun postHogActionEvent(quest: Quest, action: String) {
    PostHog.capture(
        event = PostHogUtils.POSTHOG_QUEST_SYSTEM_EVENT,
        properties = mapOf(
            PostHogUtils.POSTHOG_QUEST_NAME_ID to quest.id,
            PostHogUtils.POSTHOG_QUEST_ACTION to action
        )
    )
}

fun postHogStatusEvent(quest: Quest) {
    PostHog.capture(
        event = PostHogUtils.POSTHOG_QUEST_SYSTEM_EVENT,
        properties = mapOf(
            PostHogUtils.POSTHOG_QUEST_NAME_ID to quest.id,
            PostHogUtils.POSTHOG_QUEST_STATUS to quest.status.name
        )
    )
}

@Composable
fun ProgressQuestCard(
    modifier: Modifier = Modifier,
    directusHost: String,
    customerId: String,
    questMedicalStatus: QuestStatus = QuestStatus.PENDING,
    onEvent: (QuestSystemEvent) -> Unit = {}
) {
    val viewModel = koinViewModel<QuestSystemViewModel> { parametersOf(customerId, if (Locale.getDefault().language == "en") Constants.APP_LANGUAGE_EN else APP_LANGUAGE_ES) }
    val uiState by viewModel.state.collectAsState()
    val questsState = uiState.quests

    DisposableEffect(Unit) {
        onDispose {
            viewModel.refreshQuests()
        }
    }

    val filteredOnboardingQuests = QuestFilterUtils.filterOnboardingQuests(questsState)
    val filteredPetterQuest = QuestFilterUtils.filterQuestByFlag(filteredOnboardingQuests, POSTHOG_PETTER_FLAG, QuestValues.Petter)
    val filteredAirvetQuest = QuestFilterUtils.filterQuestByFlag(filteredPetterQuest, POSTHOG_AIRVET_FLAG, QuestValues.Airvet)
    val finalFilteredQuests = QuestFilterUtils.filterQuestByFlag(filteredAirvetQuest, POSTHOG_MEDICAL_HISTORY_FLAG, QuestValues.MedicalHistory)

    val completedQuests = finalFilteredQuests.count { it.status == QuestStatus.COMPLETED || it.status == QuestStatus.CLAIMED }
    val totalQuests = finalFilteredQuests.size
    val progress = if (totalQuests > 0) completedQuests.toFloat() / totalQuests else 0f
    val initialPage = 0

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { totalQuests }
    )

    val firstPendingIndex = finalFilteredQuests.indexOfFirst { it.status == QuestStatus.PENDING }

    LaunchedEffect(firstPendingIndex) {
        if (firstPendingIndex != -1) {
            pagerState.animateScrollToPage(firstPendingIndex)
        }
    }

    val screenWidth = 400.dp
    val pageSize = screenWidth * 0.8f
    val pageSpacing = screenWidth * 0.05f

    Column(modifier = modifier.padding(top = 20.dp)) {
        if (finalFilteredQuests.isNotEmpty()) {
            Text(
                modifier = modifier.padding(horizontal = 20.dp),
                text = stringResource(MR.strings.quest_system_title),
                fontSize = 20.sp,
                style = MobaTitle3()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryDarkest,
                    trackColor = PrimaryLight
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
        HorizontalPager(
            pageSize = PageSize.Fixed(pageSize),
            beyondViewportPageCount = totalQuests / 2,
            contentPadding = PaddingValues(horizontal = 20.dp),
            pageSpacing = pageSpacing,
            state = pagerState
        ) { page ->
            val quest = finalFilteredQuests[page]
            if (quest.status == QuestStatus.COMPLETED) {
                postHogStatusEvent(quest)
            }
            if (quest.id == QuestValues.MedicalHistory.name && quest.status == QuestStatus.PENDING && questMedicalStatus == QuestStatus.COMPLETED) {
                quest.status = questMedicalStatus
                viewModel.createQuestProgress(quest.id, QuestStatus.COMPLETED.name)
            }
            if (uiState.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ScreenLoader(
                        modifier = Modifier.fillMaxSize(),
                        color = PrimaryDarkest
                    )
                }
            }
            ProgressQuestItem(
                modifier = modifier,
                directusHost = directusHost,
                quest = quest,
                isCompleted = quest.status == QuestStatus.COMPLETED || quest.status == QuestStatus.CLAIMED,
                onEvent = onEvent,
                onClaim = {
                    viewModel.createQuestProgress(quest.id, QuestStatus.CLAIMED.name)
                },
                onBoardingCall = {
                    viewModel.createQuestProgress(quest.id, QuestStatus.COMPLETED.name)
                }
            )
        }
    }
}

@Composable
fun ProgressQuestItem(
    modifier: Modifier,
    directusHost: String,
    quest: Quest,
    isCompleted: Boolean,
    onEvent: (QuestSystemEvent) -> Unit,
    onClaim: () -> Unit,
    onBoardingCall: () -> Unit
) {
    var showConfetti by remember { mutableStateOf(false) }
    LaunchedEffect(showConfetti) {
        if (showConfetti) {
            delay(2000)
            showConfetti = false
        }
    }
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = Color.Gray,
            disabledContainerColor = Color.Gray,
            disabledContentColor = Color.Gray
        )
    ) {
        Box(
            modifier = modifier
        ) {
            if (showConfetti) {
                ConfettiAnimation(
                    modifier
                        .align(alignment = Alignment.Center)
                        .zIndex(1f),
                    isActive = true
                )
            }
            Canvas(modifier = modifier.matchParentSize()) {
                val strokeWidth = 4.dp.toPx()
                val cornerRadius = 18.dp.toPx()
                drawRoundRect(
                    color = if (quest.status == QuestStatus.CLAIMED) PrimaryLight else PrimaryDarkest,
                    topLeft = Offset(0f, 0f),
                    size = androidx.compose.ui.geometry.Size(strokeWidth, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        cornerRadius,
                        cornerRadius
                    ),
                    style = Stroke(width = strokeWidth)
                )
            }
            Column(
                modifier = modifier.padding(vertical = 12.dp),
                horizontalAlignment = Alignment.Start
            ) {
                TitleContent(modifier.padding(horizontal = 12.dp), isCompleted, quest.title, quest.titleDescription)
                Spacer(modifier = modifier.height(15.dp))
                SubTitleContent(modifier, directusHost = directusHost, quest)
                Spacer(modifier = modifier.height(30.dp))
                QuestButton(modifier.padding(horizontal = 12.dp), quest, onEvent, onClaim, onBoardingCall) { showConfetti = true }
            }
        }
    }
}

@Composable
fun TitleContent(modifier: Modifier, isCompleted: Boolean, questTitle: String, questDescription: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides Dp.Unspecified) {
            Checkbox(
                enabled = isCompleted,
                checked = isCompleted,
                onCheckedChange = { },
                colors = CheckboxDefaults.colors(
                    checkedColor = PrimaryDarkest,
                    uncheckedColor = Color.Gray
                )
            )
        }
        Text(
            modifier = modifier.padding(start = 1.dp),
            text = questTitle,
            fontSize = 14.sp,
            style = MobaTitle1()
        )
    }
    Spacer(modifier = modifier.height(10.dp))

    Text(
        modifier = modifier,
        text = questDescription,
        fontSize = 14.sp,
        style = MobaBodyRegular()
    )
}

@Composable
fun SubTitleContent(modifier: Modifier = Modifier, directusHost: String, quest: Quest) {
    var updatedSize = ""
    if (quest.id == QuestValues.MedicalHistory.name) {
        updatedSize = "?fit=cover&width=125&height=110"
    }
    Column(
        modifier = if (quest.status == QuestStatus.CLAIMED) {
            modifier
                .background(
                    PrimaryLight
                )
                .padding(vertical = 4.dp, horizontal = 12.dp)
        } else {
            modifier.padding(horizontal = 12.dp)
        }
    ) {
        Text(
            text = quest.subTitle,
            fontSize = 14.sp,
            style = MobaTitle1()
        )

        Spacer(modifier = modifier.height(16.dp))

        Row {
            AsyncImage(
                model = "https://$directusHost/assets/${quest.icon}.png$updatedSize",
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = modifier
                    .width(33.dp)
                    .height(36.dp)
            )
            Spacer(modifier = modifier.width(4.dp))
            Text(
                text = quest.subTitleDescription,
                fontSize = 14.sp,
                style = MobaBodyRegular()
            )
        }
    }
}

@Composable
fun QuestButton(
    modifier: Modifier,
    quest: Quest,
    onEvent: (QuestSystemEvent) -> Unit,
    onClaim: () -> Unit,
    onBoardingCall: () -> Unit,
    showConfetti: () -> Unit
) {
    Row {
        Spacer(modifier = modifier.weight(1f))
        if (quest.status == QuestStatus.CLAIMED) {
            Text(
                modifier = modifier.clickable(
                    onClick = {
                        if (quest.id == QuestValues.Petter.name) {
                            onEvent.invoke(QuestSystemEvent.OnPetterPressed())
                        }
                    }
                ),
                text = stringResource(MR.strings.claimed_button_text),
                fontSize = 14.sp,
                style = MobaBodyRegular()
            )
        } else {
            KanguroButton(
                modifier = modifier
                    .width(150.dp)
                    .height(40.dp),
                text = if (quest.status == QuestStatus.COMPLETED) stringResource(MR.strings.quest_claim_button) else quest.primaryButtonText,
                enabled = true,
                isQuestAvailable = quest.status != QuestStatus.COMPLETED
            ) {
                when (quest.status) {
                    QuestStatus.COMPLETED -> {
                        showConfetti()
                        onClaim()
                    }
                    QuestStatus.PENDING -> {
                        when (quest.id) {
                            QuestValues.Airvet.name -> onEvent(QuestSystemEvent.OnLiveVetPressed())
                            QuestValues.Petter.name -> onEvent(QuestSystemEvent.OnPetterPressed())
                            QuestValues.MedicalHistory.name -> onEvent(QuestSystemEvent.OnMedicalHistoryPressed())
                            QuestValues.OnboardingCall.name, QuestValues.OnboardingCallB.name -> {
                                quest.action?.let {
                                    onEvent(QuestSystemEvent.OnOnboardingCallPressed(it))
                                }
                                onBoardingCall()
                            }
                            QuestValues.Referral.name -> onEvent(QuestSystemEvent.Referral())
                        }
                        postHogActionEvent(quest, PostHogUtils.POSTHOG_QUEST_OPENED_ACTION)
                    }
                    QuestStatus.CLAIMED -> {}
                }
                postHogStatusEvent(quest)
            }
        }
    }
}
