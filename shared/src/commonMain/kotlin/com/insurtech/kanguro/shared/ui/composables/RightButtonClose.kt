package com.insurtech.kanguro.shared.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.insurtech.kanguro.shared.sharingresources.MR
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RightButtonClose(modifier: Modifier = Modifier, onClosePressed: () -> Unit) {
    IconButton(
        onClick = { onClosePressed() },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
    ) {
        Image(
            painter = dev.icerock.moko.resources.compose.painterResource(MR.images.kmp_ic_close),
            contentDescription = null
        )
    }
}

@Preview
@Composable
private fun RightClosePreview() {
    Surface {
        RightButtonClose(onClosePressed = {})
    }
}
