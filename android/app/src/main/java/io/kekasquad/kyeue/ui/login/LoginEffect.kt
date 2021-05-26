package io.kekasquad.kyeue.ui.login

import io.kekasquad.kyeue.base.MviEffect

sealed class LoginEffect : MviEffect {

    data class ChangedFormModeEffect(
        val isSignUpMode: Boolean
    ) : LoginEffect()

    data class UsernameChangedEffect(
        val username: String
    ) : LoginEffect()

    data class PasswordChangedEffect(
        val password: String
    ) : LoginEffect()

    data class FirstNameChangedEffect(
        val firstName: String
    ) : LoginEffect()

    data class LastNameChangedEffect(
        val lastName: String
    ) : LoginEffect()

    data class IsTeacherChangedEffect(
        val isTeacher: Boolean
    ) : LoginEffect()

    object LoadingEffect : LoginEffect()

    data class UsernameErrorEffect(
        val message: Int
    ) : LoginEffect()

    data class PasswordErrorEffect(
        val message: Int
    ) : LoginEffect()

    data class FirstNameErrorEffect(
        val message: Int
    ) : LoginEffect()

    data class LastNameErrorEffect(
        val message: Int
    ) : LoginEffect()

    data class MessageEffect(
        val message: Int
    ) : LoginEffect()

    object DismissErrorMessageEffect : LoginEffect()

    object NoEffect : LoginEffect()

}
