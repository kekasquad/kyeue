package io.kekasquad.kyeue.ui.login

import dagger.hilt.android.lifecycle.HiltViewModel
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.base.BaseViewModel
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import io.kekasquad.kyeue.vo.inapp.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : BaseViewModel<LoginViewState, LoginEffect, LoginIntent, LoginAction, LoginNavigationEvent>() {
    override fun initialState(): LoginViewState = LoginViewState.loginState

    override fun intentInterpreter(intent: LoginIntent): LoginAction =
        when (intent) {
            is LoginIntent.ChooseUserIsTeacherIntent -> LoginAction.ChangeIsTeacherAction(intent.isTeacher)
            is LoginIntent.InputFirstNameIntent -> LoginAction.ChangeFirstNameAction(intent.firstName)
            is LoginIntent.InputLastNameIntent -> LoginAction.ChangeLastNameAction(intent.lastName)
            is LoginIntent.InputPasswordIntent -> LoginAction.ChangePasswordAction(intent.password)
            is LoginIntent.InputUsernameIntent -> LoginAction.ChangeUsernameAction(intent.username)
            LoginIntent.LoginNothingIntent -> throw IllegalStateException("nothing intent interpreting")
            LoginIntent.OpenLoginIntent -> LoginAction.ChangeModeAction(false)
            LoginIntent.OpenSignUpIntent -> LoginAction.ChangeModeAction(true)
            LoginIntent.PerformLoginIntent -> LoginAction.PerformLoginAction
            LoginIntent.PerformSignUpIntent -> LoginAction.PerformSignUpAction
        }

    //TODO: process error from server to obtain information about problematic field
    override suspend fun performAction(action: LoginAction): LoginEffect =
        when (action) {
            is LoginAction.ChangeFirstNameAction -> LoginEffect.FirstNameChangedEffect(action.firstName)
            is LoginAction.ChangeIsTeacherAction -> LoginEffect.IsTeacherChangedEffect(action.isTeacher)
            is LoginAction.ChangeLastNameAction -> LoginEffect.LastNameChangedEffect(action.lastName)
            is LoginAction.ChangeModeAction -> LoginEffect.ChangedFormModeEffect(action.isSignUpMode)
            is LoginAction.ChangePasswordAction -> LoginEffect.PasswordChangedEffect(action.password)
            is LoginAction.ChangeUsernameAction -> LoginEffect.UsernameChangedEffect(action.username)
            LoginAction.PerformLoginAction -> {
                when {
                    viewStateLiveData.value?.username.isNullOrEmpty() ->
                        LoginEffect.UsernameErrorEffect(R.string.error_empty_field)
                    viewStateLiveData.value?.password.isNullOrEmpty() ->
                        LoginEffect.PasswordErrorEffect(R.string.error_empty_field)
                    else -> {
                        addIntermediateEffect(LoginEffect.LoadingEffect)
                        when (val result = authUseCase.loginWithCredentials(
                            username = viewStateLiveData.value!!.username,
                            password = viewStateLiveData.value!!.password
                        )) {
                            is Result.Error -> {
                                addIntermediateEffect(LoginEffect.MessageEffect(R.string.error_queue_loading_error_message))
                                delay(3000L)
                                LoginEffect.DismissErrorMessageEffect
                            }
                            is Result.Success -> {
                                navigationEventLiveData.postValue(LoginNavigationEvent.NavigateToQueue)
                                LoginEffect.NoEffect
                            }
                        }
                    }
                }
            }
            LoginAction.PerformSignUpAction -> {
                when {
                    viewStateLiveData.value?.firstName.isNullOrEmpty() ->
                        LoginEffect.FirstNameErrorEffect(R.string.error_empty_field)
                    viewStateLiveData.value?.lastName.isNullOrEmpty() ->
                        LoginEffect.LastNameErrorEffect(R.string.error_empty_field)
                    viewStateLiveData.value?.username.isNullOrEmpty() ->
                        LoginEffect.UsernameErrorEffect(R.string.error_empty_field)
                    viewStateLiveData.value?.password.isNullOrEmpty() ->
                        LoginEffect.PasswordErrorEffect(R.string.error_empty_field)
                    else -> {
                        addIntermediateEffect(LoginEffect.LoadingEffect)
                        when (val result = authUseCase.registerUser(
                            username = viewStateLiveData.value!!.username,
                            password = viewStateLiveData.value!!.lastName,
                            firstName = viewStateLiveData.value!!.firstName,
                            lastName = viewStateLiveData.value!!.lastName,
                            isTeacher = viewStateLiveData.value!!.isTeacher
                        )) {
                            is Result.Error -> {
                                addIntermediateEffect(LoginEffect.MessageEffect(R.string.error_queue_loading_error_message))
                                delay(3000L)
                                LoginEffect.DismissErrorMessageEffect
                            }
                            is Result.Success -> {
                                addIntermediateEffect(LoginEffect.MessageEffect(R.string.message_signup_success))
                                delay(3000L)
                                LoginEffect.DismissErrorMessageEffect
                            }
                        }
                    }
                }
            }
        }

    override fun stateReducer(oldState: LoginViewState, effect: LoginEffect): LoginViewState =
        when (effect) {
            is LoginEffect.ChangedFormModeEffect ->
                if (effect.isSignUpMode) LoginViewState.signUpState
                else LoginViewState.loginState
            is LoginEffect.UsernameErrorEffect -> LoginViewState.errorState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                usernameError = effect.message,
                passwordError = 0,
                firstNameError = 0,
                lastNameError = 0
            )
            is LoginEffect.FirstNameChangedEffect -> LoginViewState.inputFieldsState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = effect.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                firstNameError = oldState.firstNameError,
                usernameError = oldState.usernameError,
                passwordError = oldState.passwordError,
                lastNameError = oldState.lastNameError,
                messageText = oldState.messageText
            )
            is LoginEffect.IsTeacherChangedEffect -> LoginViewState.inputFieldsState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = effect.isTeacher,
                firstNameError = oldState.firstNameError,
                usernameError = oldState.usernameError,
                passwordError = oldState.passwordError,
                lastNameError = oldState.lastNameError,
                messageText = oldState.messageText
            )
            is LoginEffect.LastNameChangedEffect -> LoginViewState.inputFieldsState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = effect.lastName,
                isTeacher = oldState.isTeacher,
                firstNameError = oldState.firstNameError,
                usernameError = oldState.usernameError,
                passwordError = oldState.passwordError,
                lastNameError = oldState.lastNameError,
                messageText = oldState.messageText
            )
            LoginEffect.LoadingEffect -> LoginViewState.performActionState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher
            )
            LoginEffect.NoEffect -> oldState
            is LoginEffect.PasswordChangedEffect -> LoginViewState.inputFieldsState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = effect.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                firstNameError = oldState.firstNameError,
                usernameError = oldState.usernameError,
                passwordError = oldState.passwordError,
                lastNameError = oldState.lastNameError,
                messageText = oldState.messageText
            )
            is LoginEffect.UsernameChangedEffect -> LoginViewState.inputFieldsState(
                isSignUpMode = oldState.isSignUpMode,
                username = effect.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                firstNameError = oldState.firstNameError,
                usernameError = oldState.usernameError,
                passwordError = oldState.passwordError,
                lastNameError = oldState.lastNameError,
                messageText = oldState.messageText
            )
            LoginEffect.DismissErrorMessageEffect -> LoginViewState.dismissMessageState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher
            )
            is LoginEffect.FirstNameErrorEffect -> LoginViewState.errorState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                firstNameError = effect.message,
                usernameError = 0,
                passwordError = 0,
                lastNameError = 0
            )
            is LoginEffect.LastNameErrorEffect -> LoginViewState.errorState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                firstNameError = 0,
                usernameError = 0,
                passwordError = 0,
                lastNameError = effect.message
            )
            is LoginEffect.MessageEffect -> LoginViewState.messageState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                messageText = effect.message
            )
            is LoginEffect.PasswordErrorEffect -> LoginViewState.errorState(
                isSignUpMode = oldState.isSignUpMode,
                username = oldState.username,
                password = oldState.password,
                firstName = oldState.firstName,
                lastName = oldState.lastName,
                isTeacher = oldState.isTeacher,
                firstNameError = 0,
                usernameError = 0,
                passwordError = effect.message,
                lastNameError = 0
            )
        }
}