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

    fun signUpState() = LoginViewState(
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

    fun inputUsernameState(
        username: String,
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = this.usernameError,
        passwordError = this.passwordError,
        firstNameError = this.firstNameError,
        lastNameError = this.lastNameError,
        messageText = this.messageText
    )

    fun inputPasswordState(
        password: String,
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = this.usernameError,
        passwordError = this.passwordError,
        firstNameError = this.firstNameError,
        lastNameError = this.lastNameError,
        messageText = this.messageText
    )

    fun inputFirstNameState(
        firstName: String,
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = this.usernameError,
        passwordError = this.passwordError,
        firstNameError = this.firstNameError,
        lastNameError = this.lastNameError,
        messageText = this.messageText
    )

    fun inputLastNameState(
        lastName: String,
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = this.usernameError,
        passwordError = this.passwordError,
        firstNameError = this.firstNameError,
        lastNameError = this.lastNameError,
        messageText = this.messageText
    )

    fun inputIsTeacherState(
        isTeacher: Boolean
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = isTeacher,
        isLoginPerforming = false,
        usernameError = this.usernameError,
        passwordError = this.passwordError,
        firstNameError = this.firstNameError,
        lastNameError = this.lastNameError,
        messageText = this.messageText
    )

    fun performActionState() = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = true,
        usernameError = 0,
        passwordError = 0,
        firstNameError = 0,
        lastNameError = 0,
        messageText = 0
    )

    fun usernameErrorState(
        usernameError: Int
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = usernameError,
        passwordError = 0,
        firstNameError = 0,
        lastNameError = 0,
        messageText = 0
    )

    fun passwordErrorState(
        passwordError: Int
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = 0,
        passwordError = passwordError,
        firstNameError = 0,
        lastNameError = 0,
        messageText = 0
    )

    fun firstNameErrorState(
        firstNameError: Int
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = 0,
        passwordError = 0,
        firstNameError = firstNameError,
        lastNameError = 0,
        messageText = 0
    )

    fun lastNameErrorState(
        lastNameError: Int
    ) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = 0,
        passwordError = 0,
        firstNameError = 0,
        lastNameError = lastNameError,
        messageText = 0
    )

    fun messageState(messageText: Int) = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = false,
        usernameError = 0,
        passwordError = 0,
        firstNameError = 0,
        lastNameError = 0,
        messageText = messageText
    )

    fun dismissMessageState() = LoginViewState(
        isSignUpMode = this.isSignUpMode,
        username = this.username,
        password = this.password,
        firstName = this.firstName,
        lastName = this.lastName,
        isTeacher = this.isTeacher,
        isLoginPerforming = this.isLoginPerforming,
        usernameError = this.usernameError,
        passwordError = this.passwordError,
        firstNameError = this.firstNameError,
        lastNameError = this.lastNameError,
        messageText = 0
    )

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

    }

}