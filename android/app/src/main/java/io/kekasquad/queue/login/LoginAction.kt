package io.kekasquad.queue.login

import io.kekasquad.queue.base.MviAction

sealed class LoginAction : MviAction {

    data class ChangeStateAction(
        val isLogin: Boolean
    ) : LoginAction()

    data class LoginUserAction(
        val username: String,
        val password: String
    ) : LoginAction()

    data class RegisterUserAction(
        val username: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val isTeacher: Boolean
    ) : LoginAction()

}