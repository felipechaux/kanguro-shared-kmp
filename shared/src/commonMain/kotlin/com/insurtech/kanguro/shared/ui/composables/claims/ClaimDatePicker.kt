package com.insurtech.kanguro.shared.ui.composables.claims

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.KanguroButton
import com.insurtech.kanguro.shared.ui.theme.*
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun ClaimDatePicker(
    modifier: Modifier = Modifier,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        DatePickerCard(
            title = stringResource(MR.strings.incident_date_label),
            description = stringResource(MR.strings.incident_date_description),
            date = selectedDate,
            buttonText = stringResource(MR.strings.select_date_button),
            onClick = { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val date = Instant.fromEpochMilliseconds(selectedMillis)
                                .toLocalDateTime(TimeZone.currentSystemDefault()).date
                            onDateSelected(date.toString())
                            showDatePicker = false
                            showError = false
                        }
                    }
                ) {
                    Text(stringResource(MR.strings.ok_button), color = SecondaryDarkest)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(MR.strings.cancel_button), color = SecondaryDarkest)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = SecondaryDarkest
            )
        ) {
            Column {
                DatePicker(
                    state = datePickerState,
                    headline = {
                        Text(
                            text = stringResource(MR.strings.select_incident_date_headline),
                            style = MaterialTheme.typography.titleLarge,
                            color = SecondaryDarkest,
                            modifier = Modifier.padding(16.dp)
                        )
                    },
                    modifier = Modifier,
                    colors = DatePickerDefaults.colors(
                        selectedDayContainerColor = SecondaryDarkest,
                        selectedDayContentColor = Color.White,
                        todayContentColor = SecondaryDarkest,
                        todayDateBorderColor = SecondaryDarkest,
                        weekdayContentColor = SecondaryDarkest,
                        dayContentColor = SecondaryDarkest,
                        subheadContentColor = SecondaryDarkest,
                        yearContentColor = SecondaryDarkest,
                        selectedYearContainerColor = SecondaryDarkest,
                        selectedYearContentColor = Color.White
                    )
                )
                if (showError) {
                    Text(
                        text = stringResource(MR.strings.selected_date_not_allowed_error),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun DatePickerCard(
    title: String,
    description: String,
    date: String,
    buttonText: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, SecondaryLight.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = NeutralBackground)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MSansSemiBoldSecondaryDarkest21(),
                color = SecondaryDarkest
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = description,
                style = MobaBodyRegular(),
                color = SecondaryMedium
            )
            Spacer(Modifier.height(12.dp))
            KanguroButton(
                text = buttonText,
                enabled = true,
                onClick = onClick,
                modifier = Modifier.align(Alignment.End)
            )
            if (date.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(MR.strings.selected_label),
                        style = MobaBodyRegular().copy(fontWeight = FontWeight.Bold),
                        color = SecondaryDarkest
                    )
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = date,
                        style = MobaBodyRegular(),
                        color = PrimaryDarkest
                    )
                }
            }
        }
    }
}
