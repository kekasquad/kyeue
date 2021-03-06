package io.kekasquad.kyeue.base

import androidx.lifecycle.LiveData

interface MviView<
        VS : MviViewState,
        I : MviIntent,
        NE : MviNavigationEvent> {
    fun render(viewState: VS)
    fun intents(): LiveData<I>
    fun navigator(navigationEvent: NE)
}