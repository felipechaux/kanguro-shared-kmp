package com.insurtech.kanguro.shared.ui.composables.liveVet

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.insurtech.kanguro.shared.data.questSystem.model.QuestStatus
import com.insurtech.kanguro.shared.data.questSystem.model.QuestValues
import com.insurtech.kanguro.shared.ui.composables.questSystem.QuestSystemViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SharedLiveVetBottomSheet(
    modifier: Modifier = Modifier.fillMaxSize(),
    customerId: String,
    onEvent: () -> Unit = {},
    onClosePressed: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    val questViewModel = koinViewModel<QuestSystemViewModel> { parametersOf(customerId, "") }
    val viewModel = viewModel {
        LiveVetViewModel()
    }
    val liveVetUiState by viewModel.state.collectAsState()

    LiveVetBottomSheetScreenContent(
        modifier = modifier,
        isLoading = liveVetUiState.isLoading,
        isError = liveVetUiState.isError,
        onEvent = {
            scope.launch {
                questViewModel.createQuestProgress(QuestValues.Airvet.name, QuestStatus.COMPLETED.name)
            }
            onEvent()
        },
        onClosePressed = onClosePressed
    )
}
