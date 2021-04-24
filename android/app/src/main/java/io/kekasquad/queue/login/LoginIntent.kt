package io.kekasquad.queue.login

import io.kekasquad.queue.base.MviIntent
import io.kekasquad.queue.base.NothingIntent

sealed class LoginIntent : MviIntent {

    object LoginNothingIntent : LoginIntent(), NothingIntent

    object SwitchToLoginUserIntent : LoginIntent()

    object SwitchToRegisterUserIntent : LoginIntent()

    data class LoginUserIntent(
        val username: String,
        val password: String
    ) : LoginIntent()

    data class RegisterUserIntent(
        val username: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val isTeacher: Boolean
    ) : LoginIntent()

}