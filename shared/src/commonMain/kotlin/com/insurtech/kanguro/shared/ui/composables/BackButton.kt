package com.insurtech.kanguro.shared.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.insurtech.kanguro.shared.sharingresources.MR
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    IconButton(
        onClick = { onBackPressed() },
        modifier = modifier
            .wrapContentWidth(Alignment.Start)
            .size(24.dp)
    ) {
        Image(
            painter = dev.icerock.moko.resources.compose.painterResource(MR.images.kmp_ic_back_arrow),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun BackButtonPreview() {
    Surface {
        BackButton {
        }
    }
}
