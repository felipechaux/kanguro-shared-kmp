package com.insurtech.kanguro.shared.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.insurtech.kanguro.shared.ui.theme.MobaBodyRegular
import com.insurtech.kanguro.shared.ui.theme.SecondaryDarkest
import com.insurtech.kanguro.shared.ui.theme.SecondaryLight

@Composable
fun KanguroTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        label = label?.let { { Text(it, style = MobaBodyRegular(), color = SecondaryDarkest) } },
        placeholder = placeholder?.let { { Text(it, style = MobaBodyRegular(), color = SecondaryLight) } },
        isError = isError,
        singleLine = singleLine,
        textStyle = MobaBodyRegular().copy(fontSize = 16.sp, color = SecondaryDarkest),
        visualTransformation = visualTransformation,
        enabled = enabled,
        supportingText = if (isError && errorMessage != null) {
            { Text(errorMessage, color = Color.Red, style = MobaBodyRegular()) }
        } else {
            null
        },
        shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
        keyboardOptions = keyboardOptions
    )
}