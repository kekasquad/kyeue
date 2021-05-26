package io.kekasquad.kyeue.ui.login

import androidx.annotation.StringRes
import io.kekasquad.kyeue.base.MviViewState

data class LoginViewState(
    val isSignUpMode: Boolean,
    val username: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val isTeacher: Boolean,
    val isLoginPerforming: Boolean,
    @StringRes val usernameError: Int,
    @StringRes val passwordError: Int,
    @StringRes val firstNameError: Int,
    @StringRes val lastNameError: Int,
    @StringRes val messageText: Int
) : MviViewState {

    companion object {
        val loginState = LoginViewState(
            isSignUpMode = false,
            username = "",
            password = "",
            firstName = "",
            lastName = "",
            isTeacher = false,
            isLoginPerforming = false,
            usernameError = 0,
            passwordError = 0,
            firstNameError = 0,
            lastNameError = 0,
            messageText = 0
        )

        val signUpState = LoginViewState(
            isSignUpMode = true,
            username = "",
            password = "",
            firstName = "",
            lastName = "",
            isTeacher = false,
            isLoginPerforming = false,
            usernameError = 0,
            passwordError = 0,
            firstNameError = 0,
            lastNameError = 0,
            messageText = 0
        )

        fun inputFieldsState(
            isSignUpMode: Boolean,
            username: String,
            password: String,
            firstName: String,
            lastName: String,
            isTeacher: Boolean,
            usernameError: Int,
            passwordError: Int,
            firstNameError: Int,
            lastNameError: Int,
            messageText: Int
        ) = LoginViewState(
            isSignUpMode = isSignUpMode,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            isTeacher = isTeacher,
            isLoginPerforming = false,
            usernameError = usernameError,
            passwordError = passwordError,
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            messageText = messageText
        )

        fun performActionState(
            isSignUpMode: Boolean,
            username: String,
            password: String,
            firstName: String,
            lastName: String,
            isTeacher: Boolean
        ) = LoginViewState(
            isSignUpMode = isSignUpMode,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            isTeacher = isTeacher,
            isLoginPerforming = true,
            usernameError = 0,
            passwordError = 0,
            firstNameError = 0,
            lastNameError = 0,
            messageText = 0
        )

        fun errorState(
            isSignUpMode: Boolean,
            username: String,
            password: String,
            firstName: String,
            lastName: String,
            isTeacher: Boolean,
            usernameError: Int,
            passwordError: Int,
            firstNameError: Int,
            lastNameError: Int
        ) = LoginViewState(
            isSignUpMode = isSignUpMode,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            isTeacher = isTeacher,
            isLoginPerforming = false,
            usernameError = usernameError,
            passwordError = passwordError,
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            messageText = 0
        )

        fun messageState(
            isSignUpMode: Boolean,
            username: String,
            password: String,
            firstName: String,
            lastName: String,
            isTeacher: Boolean,
            messageText: Int
        ) = LoginViewState(
            isSignUpMode = isSignUpMode,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            isTeacher = isTeacher,
            isLoginPerforming = false,
            usernameError = 0,
            passwordError = 0,
            firstNameError = 0,
            lastNameError = 0,
            messageText = messageText
        )

        fun dismissMessageState(
            isSignUpMode: Boolean,
            username: String,
            password: String,
            firstName: String,
            lastName: String,
            isTeacher: Boolean
        ) = LoginViewState(
            isSignUpMode = isSignUpMode,
            username = username,
            password = password,
            firstName = firstName,
            lastName = lastName,
            isTeacher = isTeacher,
            isLoginPerforming = false,
            usernameError = 0,
            passwordError = 0,
            firstNameError = 0,
            lastNameError = 0,
            messageText = 0
        )

    }

}