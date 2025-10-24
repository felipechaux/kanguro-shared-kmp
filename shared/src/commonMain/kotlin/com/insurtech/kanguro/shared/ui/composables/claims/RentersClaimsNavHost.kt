
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.insurtech.kanguro.shared.domain.claims.StartDestination
import com.insurtech.kanguro.shared.ui.composables.KanguroButton
import com.insurtech.kanguro.shared.ui.composables.claims.model.ClaimRentersPolicy
import com.insurtech.kanguro.shared.ui.composables.claims.model.UseProfile
import com.insurtech.kanguro.shared.ui.composables.claims.screens.ContactInfoScreen
import com.insurtech.kanguro.shared.ui.composables.claims.screens.MyClaimsScreen
import com.insurtech.kanguro.shared.ui.composables.claims.screens.RentersClaimsViewModel
import com.insurtech.kanguro.shared.ui.composables.claims.screens.SelectPolicyScreen
import com.insurtech.kanguro.shared.ui.composables.claims.screens.UploadDocumentsScreen
import com.insurtech.kanguro.shared.ui.theme.White
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RentersClaimsNavHost(
    startDestination: StartDestination = StartDestination(destination = SelectPolicyScreen),
    rentersPolicy: List<ClaimRentersPolicy>,
    userProfile: UseProfile,
    phoneUpdated: (phone: String) -> Unit = {},
    onClose: () -> Unit
) {
    val navController = rememberNavController()
    val viewModel = koinViewModel<RentersClaimsViewModel>()

    LaunchedEffect(Unit) {
        viewModel.setPoliciesList(rentersPolicy.map { it.id })
    }

    NavHost(
        navController = navController,
        startDestination = startDestination.destination
    ) {
        composable<SelectPolicyScreen> {
            SelectPolicyScreen(
                rentersPolicy = rentersPolicy,
                onNext = { navController.navigate(DescribeIncidentScreen) },
                onClose = onClose,
                viewModel = viewModel
            )
        }
        composable<DescribeIncidentScreen> {
            DescribeIncidentScreen(
                onNext = { navController.navigate(UploadDocumentsScreen) },
                onBack = { navController.popBackStack() },
                onClose = onClose,
                viewModel = viewModel
            )
        }
        composable<UploadDocumentsScreen> {
            UploadDocumentsScreen(
                onNext = { navController.navigate(ContactInfoScreen) },
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable<ContactInfoScreen> {
            ContactInfoScreen(
                useProfile = userProfile,
                phoneUpdated = phoneUpdated,
                onNext = { navController.navigate(MyClaimsScreen) },
                onBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
        composable<MyClaimsScreen> {
            MyClaimsScreen(
                viewModel = viewModel,
                onCreateNewClaim = { navController.navigate(SelectPolicyScreen) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenScaffold(
    onNext: (() -> Unit)? = null,
    onBack: (() -> Unit)? = null,
    onClose: (() -> Unit)? = null, // Add this parameter
    nextLabel: String = "Next",
    showBack: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = White
                ),
                title = {},
                navigationIcon = {
                    if (showBack && onBack != null && onClose == null) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                },
                actions = {
                    if (onClose != null) {
                        IconButton(onClick = onClose) {
                            Icon(Icons.Filled.Close, contentDescription = "Close")
                        }
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = White)
                    .padding(padding),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                content = content
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (onNext != null) {
                    KanguroButton(
                        text = nextLabel,
                        enabled = true,
                        onClick = onNext
                    )
                }
            }
        }
    )
}
