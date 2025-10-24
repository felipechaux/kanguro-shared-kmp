package com.insurtech.kanguro.shared.ui.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.insurtech.kanguro.shared.ui.theme.MobaSubheadRegular
import com.insurtech.kanguro.shared.ui.theme.NeutralBackground
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import kotlinx.coroutines.delay

@Composable
fun CustomToast(
    message: String,
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = NeutralBackground,
    textColor: Color = SecondaryDarkest,
    borderColor: Color = SecondaryDarkest.copy(alpha = 0.3f),
    onDismiss: () -> Unit = {}
) {
    var showToast by remember(isVisible) { mutableStateOf(isVisible) }

    LaunchedEffect(isVisible) {
        if (isVisible) {
            showToast = true
            delay(5000)
            showToast = false
            delay(300)
            onDismiss()
        }
    }

    AnimatedVisibility(
        modifier = modifier,
        visible = showToast,
        enter = slideInVertically(
            initialOffsetY = { it }
        ) + fadeIn(),
        exit = slideOutVertically(
            targetOffsetY = { it }
        ) + fadeOut()
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .zIndex(Float.MAX_VALUE),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(1.dp, borderColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = message,
                    style = MobaSubheadRegular(),
                    color = textColor,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp
                )
            }
        }
    }
}
