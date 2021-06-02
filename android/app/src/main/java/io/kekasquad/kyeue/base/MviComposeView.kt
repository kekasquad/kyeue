package io.kekasquad.kyeue.base

import androidx.compose.runtime.Composable
import androidx.lifecycle.LiveData

interface MviComposeView<
        VS : MviViewState,
        I  : MviIntent,
        NE : MviNavigationEvent> {
    val render: @Composable ((VS) -> Unit)
    fun intents(): LiveData<I>
    fun navigator(navigationEvent: NE)
}