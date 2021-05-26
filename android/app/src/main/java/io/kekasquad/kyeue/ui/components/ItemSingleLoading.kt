package io.kekasquad.kyeue.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import io.kekasquad.kyeue.ui.theme.KyeueTheme

@Composable
fun ItemSingleLoading() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val content = createRef()

        CircularProgressIndicator(
            modifier = Modifier.constrainAs(content) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            color = MaterialTheme.colors.primary
        )
    }
}

@Preview
@Composable
fun PreviewItemSingleLoading() {
    KyeueTheme {
        ItemSingleLoading()
    }
}