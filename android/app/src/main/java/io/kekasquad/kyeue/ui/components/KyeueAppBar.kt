package io.kekasquad.kyeue.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import io.kekasquad.kyeue.ui.theme.KyeueTheme

@Composable
fun KyeueAppBar(
    modifier: Modifier = Modifier,
    title: @Composable RowScope.() -> Unit,
    actions: @Composable RowScope.() -> Unit = {},
    navigation: @Composable (() -> Unit)? = null
) {
    Column {
        Spacer(
            modifier = modifier
                .statusBarsHeight()
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary)
        )
        TopAppBar(
            modifier = modifier,
            backgroundColor = MaterialTheme.colors.primary,
            elevation = 16.dp,
            contentColor = MaterialTheme.colors.onPrimary,
            actions = actions,
            title = { Row { title() } },
            navigationIcon = navigation
        )
    }
}

@Preview
@Composable
fun KyeueAppBarPreview() {
    KyeueTheme {
        KyeueAppBar(title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .absolutePadding(right = 16.dp),
                text = "Preview!",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        })
    }
}

@Preview
@Composable
fun KyeueAppBarPreviewDark() {
    KyeueTheme(isDarkTheme = true) {
        KyeueAppBar(title = {
            Text(
                "Preview!",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary
            )
        })
    }
}