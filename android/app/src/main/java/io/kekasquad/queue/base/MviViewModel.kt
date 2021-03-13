package io.kekasquad.queue.base

import androidx.lifecycle.LiveData

interface MviViewModel<
        VS : MviViewState,
        I : MviIntent> {
    fun processIntents(intents: LiveData<I>)
    fun states(): LiveData<VS>
}