package io.kekasquad.kyeue.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData

interface MviView<
        VS : MviViewState,
        I  : MviIntent,
        NE : MviNavigationEvent> {
    val render: @Composable ((VS) -> Unit)
    fun intents(): LiveData<I>
    fun navigator(navigationEvent: NE)
}