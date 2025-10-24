package com.insurtech.kanguro.shared.ui.composables.pharmacy

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insurtech.kanguro.shared.data.pharmacy.api.PharmacyRequest
import com.insurtech.kanguro.shared.sharingresources.MR
import com.insurtech.kanguro.shared.ui.composables.ScreenLoader
import com.insurtech.kanguro.shared.ui.theme.MobaTitle3
import com.insurtech.kanguro.shared.ui.theme.MuseoSans15RegularSecondaryDark
import com.insurtech.kanguro.shared.ui.theme.NeutralBackground
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import com.insurtech.kanguro.shared.ui.theme.TextButtonStyle
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
private fun PharmacyHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Kanguro logo
        Image(
            painter = painterResource(MR.images.ic_pharmacy),
            contentDescription = stringResource(MR.strings.pharmacy_logo_content_description),
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun PharmacyTitle(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(MR.strings.pharmacy_intro_1),
            style = MuseoSans15RegularSecondaryDark(),
            color = SecondaryDarkest,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Text(
            text = stringResource(MR.strings.pharmacy_intro_2),
            style = MobaTitle3(),
            color = SecondaryDarkest,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
    }
}

@Composable
private fun ServiceItem(
    icon: @Composable () -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = text,
            style = MuseoSans15RegularSecondaryDark(),
            color = SecondaryDarkest,
            fontSize = 17.sp,
            lineHeight = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
    }
}

@Composable
private fun PharmacyServices(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        ServiceItem(
            icon = {
                // Pill bottle and blister pack icon
                Image(
                    painter = painterResource(MR.images.ic_medications),
                    contentDescription = stringResource(MR.strings.pharmacy_medication_icon_content_description),
                    modifier = Modifier.size(24.dp)
                )
            },
            text = stringResource(MR.strings.pharmacy_service_1)
        )

        ServiceItem(
            icon = {
                // Single capsule icon
                Image(
                    painter = painterResource(MR.images.ic_supplements),
                    contentDescription = stringResource(MR.strings.pharmacy_supplement_icon_content_description),
                    modifier = Modifier.size(24.dp)
                )
            },
            text = stringResource(MR.strings.pharmacy_service_2)
        )

        ServiceItem(
            icon = {
                // Hand holding paw icon
                Image(
                    painter = painterResource(MR.images.ic_care),
                    contentDescription = stringResource(MR.strings.pharmacy_wellness_icon_content_description),
                    modifier = Modifier.size(24.dp)
                )
            },
            text = stringResource(MR.strings.pharmacy_service_3)
        )

        ServiceItem(
            icon = {
                // Mobile phone with shopping cart icon
                Image(
                    painter = painterResource(MR.images.ic_more_device),
                    contentDescription = stringResource(MR.strings.pharmacy_shopping_icon_content_description),
                    modifier = Modifier.size(24.dp)
                )
            },
            text = stringResource(MR.strings.pharmacy_service_4)
        )
    }
}

@Composable
private fun PharmacyConvenienceText(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(MR.strings.pharmacy_convenience),
        style = MuseoSans15RegularSecondaryDark(),
        color = SecondaryDarkest,
        textAlign = TextAlign.Center,
        fontSize = 20.sp,
        lineHeight = 26.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier.fillMaxWidth().padding(top = 8.dp)
    )
}

@Composable
fun PharmacyCloseButton(
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
                contentDescription = stringResource(MR.strings.pharmacy_close_content_description)
            )
        }
    }
}

@Composable
fun PharmacyView(
    pharmacyRequest: PharmacyRequest,
    onNavigateBack: () -> Unit,
    onPharmacyViewOpened: () -> Unit = {},
    onPharmacyWebViewLoaded: () -> Unit = {},
    errorPharmacy: (String) -> Unit = {}
) {
    val pharmacyViewModel = koinViewModel<PharmacyViewModel>()
    var showWebView by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        onPharmacyViewOpened()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (pharmacyViewModel.state.loading) {
            ScreenLoader(
                modifier = Modifier.fillMaxSize(),
                color = NeutralBackground
            )
        } else {
            if (showWebView) {
                val pharmacyLink = pharmacyViewModel.state.pharmacyLink
                val webViewState = pharmacyLink?.let { rememberWebViewState(it) }
                if (webViewState != null) {
                    onPharmacyWebViewLoaded()
                    WebView(
                        state = webViewState,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    errorPharmacy(stringResource(MR.strings.pharmacy_error_loading))
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(MR.strings.pharmacy_error_loading),
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
                            .padding(bottom = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            PharmacyHeader(modifier = Modifier.padding(top = 24.dp))
                            Spacer(modifier = Modifier.height(24.dp))
                            PharmacyTitle()
                            Spacer(modifier = Modifier.height(32.dp))
                            Spacer(modifier = Modifier.height(24.dp))
                            PharmacyServices()
                            Spacer(modifier = Modifier.height(32.dp))
                            PharmacyConvenienceText()
                            Spacer(modifier = Modifier.height(32.dp))
                        }

                        Image(
                            painter = painterResource(MR.images.ic_dog_hand),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        color = Color.Transparent
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    pharmacyViewModel.fetchPharmacyLink(pharmacyRequest)
                                    showWebView = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFE3F2FD) // Light blue color from design
                                ),
                                shape = RoundedCornerShape(28.dp)
                            ) {
                                Text(
                                    text = stringResource(MR.strings.pharmacy_cta_button),
                                    style = TextButtonStyle(),
                                    color = SecondaryDarkest,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }

        PharmacyCloseButton(
            onNavigateBack = onNavigateBack,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        )
    }
}
