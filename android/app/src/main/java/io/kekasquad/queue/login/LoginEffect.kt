package io.kekasquad.queue.login

import io.kekasquad.queue.base.MviEffect

sealed class LoginEffect : MviEffect {

    object NothingEffect : LoginEffect()

    data class ChangeStateEffect(
        val isLogin: Boolean
    ) : LoginEffect()

    object AuthProceedingEffect : LoginEffect()

    data class AuthProceedingErrorEffect(
        val error: Throwable
    ) : LoginEffect()

    object UserRegisteredEffect : LoginEffect()

    object HideRegisteredMessageEffect : LoginEffect()

}