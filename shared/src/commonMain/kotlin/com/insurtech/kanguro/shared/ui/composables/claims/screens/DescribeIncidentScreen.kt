
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.KanguroTextField
import com.insurtech.kanguro.shared.ui.composables.claims.ClaimDatePicker
import com.insurtech.kanguro.shared.ui.composables.claims.screens.RentersClaimsViewModel
import com.insurtech.kanguro.shared.ui.theme.*
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.serialization.Serializable

@Serializable
object DescribeIncidentScreen

@Composable
fun DescribeIncidentScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    viewModel: RentersClaimsViewModel,
    onClose: (() -> Unit)? = null
) {
    val incidentDate by viewModel.incidentDate.collectAsState()
    val showCLoseNext by viewModel.showCloseButtonNext.collectAsState()
    val description by viewModel.incidentDescription.collectAsState()
    var localDescription by remember { mutableStateOf(TextFieldValue(description ?: "")) }
    var localDate by remember { mutableStateOf(incidentDate ?: "") }

    ScreenScaffold(
        onNext = {
            if (localDescription.text.isNotBlank() && localDate.isNotBlank()) {
                viewModel.setIncidentDescription(localDescription.text)
                viewModel.setIncidentDate(localDate)
                onNext()
            }
        },
        onBack = if (!showCLoseNext) onBack else null,
        onClose = if (showCLoseNext) onClose else null,
        showBack = showCLoseNext
    ) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(MR.strings.describe_incident_title),
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = SecondaryDarkest,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 24.dp),
                thickness = 2.dp,
                color = SecondaryDarkest
            )
            // Date Input using DatePickerDemo
            ClaimDatePicker(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                selectedDate = localDate,
                onDateSelected = {
                    localDate = it
                }
            )
            Spacer(modifier = Modifier.height(spacingXxxs))

            Text(
                text = stringResource(MR.strings.describe_incident_description_label),
                style = MobaSubheadBold(),
                color = SecondaryDarkest,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(spacingNanoLarge))
            KanguroTextField(
                value = localDescription.text,
                onValueChange = { localDescription = TextFieldValue(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                label = null,
                placeholder = stringResource(MR.strings.describe_incident_placeholder),
                singleLine = false
            )
        }
    }
}
