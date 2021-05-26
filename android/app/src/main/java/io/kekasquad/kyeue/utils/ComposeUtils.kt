package io.kekasquad.kyeue.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

@Composable
@ReadOnlyComposable
fun stringResourceOrNull(@StringRes id: Int): String? =
    if (id != 0) stringResource(id = id) else null