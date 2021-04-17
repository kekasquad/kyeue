package io.kekasquad.queue.queues

import io.kekasquad.queue.base.MviAction

sealed class QueuesAction : MviAction {

    object InitialLoadingAction : QueuesAction()

    object PagingLoadingAction : QueuesAction()

}