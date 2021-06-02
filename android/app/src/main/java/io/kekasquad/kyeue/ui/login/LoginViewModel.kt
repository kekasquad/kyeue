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
                                addIntermediateEffect(LoginEffect.MessageEffect(R.string.error_login))
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
                                addIntermediateEffect(LoginEffect.MessageEffect(R.string.error_sign_up))
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
                if (effect.isSignUpMode) oldState.signUpState()
                else LoginViewState.loginState
            is LoginEffect.UsernameErrorEffect -> oldState.usernameErrorState(
                usernameError = effect.message
            )
            is LoginEffect.FirstNameChangedEffect -> oldState.inputFirstNameState(
                firstName = effect.firstName
            )
            is LoginEffect.IsTeacherChangedEffect -> oldState.inputIsTeacherState(
                isTeacher = effect.isTeacher
            )
            is LoginEffect.LastNameChangedEffect -> oldState.inputLastNameState(
                lastName = effect.lastName
            )
            LoginEffect.LoadingEffect -> oldState.performActionState()
            LoginEffect.NoEffect -> oldState
            is LoginEffect.PasswordChangedEffect -> oldState.inputPasswordState(
                password = effect.password
            )
            is LoginEffect.UsernameChangedEffect -> oldState.inputUsernameState(
                username = effect.username
            )
            LoginEffect.DismissErrorMessageEffect -> oldState.dismissMessageState()
            is LoginEffect.FirstNameErrorEffect -> oldState.firstNameErrorState(
                firstNameError = effect.message
            )
            is LoginEffect.LastNameErrorEffect -> oldState.lastNameErrorState(
                lastNameError = effect.message
            )
            is LoginEffect.MessageEffect -> oldState.messageState(
                messageText = effect.message
            )
            is LoginEffect.PasswordErrorEffect -> oldState.passwordErrorState(
                passwordError = effect.message
            )
        }
}