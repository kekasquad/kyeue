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
import io.kekasquad.kyeue.ui.theme.KyeueTheme

@Composable
fun ItemSingleLoadingError(
    errorMessage: @Composable () -> Unit,
    onRetry: () -> Unit
) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val content = createRef()

        Row(
            modifier = Modifier.constrainAs(content) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            errorMessage()
            Button(
                modifier = Modifier.padding(16.dp),
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
}

@Preview
@Composable
fun PreviewItemSingleLoadingError() {
    KyeueTheme {
        ItemSingleLoadingError(
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