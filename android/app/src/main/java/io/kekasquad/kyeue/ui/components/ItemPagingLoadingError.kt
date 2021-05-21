package io.kekasquad.kyeue.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import io.kekasquad.kyeue.ui.theme.KyeueTheme

@Composable
fun ItemPagingLoadingError(
    errorMessage: @Composable () -> Unit,
    onRetry: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .height(72.dp)
            .fillMaxWidth()
    ) {
        val (message, button) = createRefs()
        Row(
            modifier = Modifier.constrainAs(message) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(button.start, 16.dp)
                start.linkTo(parent.start, 16.dp)
                width = Dimension.fillToConstraints
            }
        ) {
            errorMessage()
        }
        Button(
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end, 16.dp)
            },
            onClick = onRetry
        ) {
            Text(
                text = "Retry",
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.button
            )
        }
    }
}

@Preview
@Composable
fun PreviewItemPagingLoadingError() {
    KyeueTheme {
        ItemPagingLoadingError(
            errorMessage =
            {
                Text(
                    text = "Error loading",
                    color = MaterialTheme.colors.onSurface,
                    style = MaterialTheme.typography.h6
                )
            },
            onRetry = { }
        )
    }
}