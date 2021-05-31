package io.kekasquad.kyeue.ui.queuedetails

import io.kekasquad.kyeue.base.MviNavigationEvent

sealed class QueueDetailsNavigationEvent : MviNavigationEvent {

    object NavigateBackEvent : QueueDetailsNavigationEvent()

}