package io.kekasquad.kyeue.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData

interface MviView<
        VS : MviViewState,
        I : MviIntent> {
    val render: @Composable ((VS) -> Unit)
    fun intents(): LiveData<I>
}