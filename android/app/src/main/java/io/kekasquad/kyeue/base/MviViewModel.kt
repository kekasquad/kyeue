package io.kekasquad.kyeue.base

import androidx.lifecycle.LiveData

interface MviViewModel<
        VS : MviViewState,
        I : MviIntent,
        NE : MviNavigationEvent> {
    fun processIntents(intents: LiveData<I>)
    fun states(): LiveData<VS>
    fun navigationEvents(): LiveData<NE>
}