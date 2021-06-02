package io.kekasquad.kyeue.ui

import io.kekasquad.kyeue.base.MviNavigationEvent

sealed class MainActivityNavigationEvent : MviNavigationEvent {

    object NavigateToQueueEvent : MainActivityNavigationEvent()

    object NavigateToLoginEvent : MainActivityNavigationEvent()

}