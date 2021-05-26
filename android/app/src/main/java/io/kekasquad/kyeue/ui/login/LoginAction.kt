package io.kekasquad.kyeue.ui.login

import io.kekasquad.kyeue.base.MviAction

sealed class LoginAction : MviAction {

    data class ChangeModeAction(
        val isSignUpMode: Boolean
    ) : LoginAction()

    data class ChangeUsernameAction(
        val username: String
    ) : LoginAction()

    data class ChangePasswordAction(
        val password: String
    ) : LoginAction()

    data class ChangeFirstNameAction(
        val firstName: String
    ) : LoginAction()

    data class ChangeLastNameAction(
        val lastName: String
    ) : LoginAction()

    data class ChangeIsTeacherAction(
        val isTeacher: Boolean
    ) : LoginAction()

    object PerformLoginAction : LoginAction()

    object PerformSignUpAction : LoginAction()

}