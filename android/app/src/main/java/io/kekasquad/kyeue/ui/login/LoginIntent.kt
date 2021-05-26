package io.kekasquad.kyeue.ui.login

import io.kekasquad.kyeue.base.MviIntent
import io.kekasquad.kyeue.base.NothingIntent

sealed class LoginIntent : MviIntent {

    object OpenSignUpIntent : LoginIntent()

    object OpenLoginIntent : LoginIntent()

    data class InputUsernameIntent(
        val username: String
    ) : LoginIntent()

    data class InputPasswordIntent(
        val password: String
    ) : LoginIntent()

    data class InputFirstNameIntent(
        val firstName: String
    ) : LoginIntent()

    data class InputLastNameIntent(
        val lastName: String
    ) : LoginIntent()

    data class ChooseUserIsTeacherIntent(
        val isTeacher: Boolean
    ) : LoginIntent()

    object PerformLoginIntent : LoginIntent()

    object PerformSignUpIntent : LoginIntent()

    object LoginNothingIntent : LoginIntent(), NothingIntent

}