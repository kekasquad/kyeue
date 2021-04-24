package io.kekasquad.queue.queue

import io.kekasquad.queue.base.MviAction

sealed class QueueAction : MviAction {

    data class InitialLoadingAction(
        val id: String
    ) : QueueAction()

    object NavigateBackAction : QueueAction()

}