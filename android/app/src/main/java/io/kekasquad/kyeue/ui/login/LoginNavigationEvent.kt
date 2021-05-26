package io.kekasquad.kyeue.ui.login

import io.kekasquad.kyeue.base.MviNavigationEvent

sealed class LoginNavigationEvent : MviNavigationEvent {

    object NavigateToQueue : LoginNavigationEvent()

}
