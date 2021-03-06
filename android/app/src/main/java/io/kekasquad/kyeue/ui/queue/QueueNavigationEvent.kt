package io.kekasquad.kyeue.ui.queue

import io.kekasquad.kyeue.base.MviNavigationEvent

sealed class QueueNavigationEvent : MviNavigationEvent {

    data class NavigateToQueueDetailsEvent(
        val queueId: String
    ) : QueueNavigationEvent()

    object NavigateToLoginEvent : QueueNavigationEvent()

}