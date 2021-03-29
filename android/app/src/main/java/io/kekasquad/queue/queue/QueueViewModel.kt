package io.kekasquad.queue.queue

import androidx.hilt.lifecycle.ViewModelInject
import io.kekasquad.queue.base.BaseViewModel
import io.kekasquad.queue.nav.Coordinator

class QueueViewModel @ViewModelInject constructor(
    private val coordinator: Coordinator
) : BaseViewModel<QueueViewState, QueueEffect, QueueIntent, QueueAction>() {
    override fun initialState(): QueueViewState = QueueViewState.initialState

    override fun intentInterpreter(intent: QueueIntent): QueueAction {
        TODO("Not yet implemented")
    }

    override suspend fun performAction(action: QueueAction): QueueEffect {
        TODO("Not yet implemented")
    }

    override fun stateReducer(oldState: QueueViewState, effect: QueueEffect): QueueViewState {
        TODO("Not yet implemented")
    }


}