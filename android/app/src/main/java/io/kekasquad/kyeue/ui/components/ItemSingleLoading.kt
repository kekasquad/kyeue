package io.kekasquad.kyeue.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.kekasquad.kyeue.ui.theme.KyeueTheme

@Composable
fun ItemSingleLoading() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(color = MaterialTheme.colors.primary)
    }
}

@Preview
@Composable
fun PreviewItemSingleLoading() {
    KyeueTheme {
        ItemSingleLoading()
    }
}