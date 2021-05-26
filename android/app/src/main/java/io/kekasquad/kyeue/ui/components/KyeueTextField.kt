package io.kekasquad.kyeue.ui.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun KyueuTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    error: String? = null,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors()
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            singleLine = singleLine,
            textStyle = MaterialTheme.typography.subtitle1,
            label = label?.let { { TextFieldLabel(label = it) } },
            placeholder = placeholder?.let { { TextFieldPlaceholder(placeholder = it) } },
            isError = error != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            interactionSource = interactionSource,
            colors = colors
        )

        error?.let {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                text = it,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun TextFieldLabel(label: String) {
    Text(text = label)
}

@Composable
private fun TextFieldPlaceholder(placeholder: String) {
    Text(text = placeholder)
}