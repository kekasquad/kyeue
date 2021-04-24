package io.kekasquad.queue.login

import androidx.hilt.lifecycle.ViewModelInject
import io.kekasquad.queue.base.BaseViewModel
import io.kekasquad.queue.data.usecase.AuthUseCase
import io.kekasquad.queue.nav.Coordinator
import io.kekasquad.queue.vo.inapp.Result
import kotlinx.coroutines.delay

class LoginViewModel @ViewModelInject constructor(
    private val coordinator: Coordinator,
    private val authUseCase: AuthUseCase
) : BaseViewModel<LoginViewState, LoginEffect, LoginIntent, LoginAction>() {
    override fun initialState(): LoginViewState = LoginViewState()

    override fun intentInterpreter(intent: LoginIntent): LoginAction =
        when (intent) {
            is LoginIntent.LoginUserIntent -> LoginAction.LoginUserAction(
                intent.username,
                intent.password
            )
            is LoginIntent.RegisterUserIntent -> LoginAction.RegisterUserAction(
                intent.username,
                intent.password,
                intent.firstName,
                intent.lastName,
                intent.isTeacher
            )
            LoginIntent.SwitchToLoginUserIntent -> LoginAction.ChangeStateAction(true)
            LoginIntent.SwitchToRegisterUserIntent -> LoginAction.ChangeStateAction(false)
            LoginIntent.LoginNothingIntent -> throw IllegalArgumentException("nothing intent interpreting")
        }

    override suspend fun performAction(action: LoginAction): LoginEffect =
        when (action) {
            is LoginAction.ChangeStateAction -> LoginEffect.ChangeStateEffect(action.isLogin)
            is LoginAction.LoginUserAction -> {
                addIntermediateEffect(LoginEffect.AuthProceedingEffect)
                when (val result =
                    authUseCase.loginWithCredentials(action.username, action.password)) {
                    is Result.Error -> LoginEffect.AuthProceedingErrorEffect(result.throwable)
                    is Result.Success -> {
                        coordinator.navigateToQueues()
                        LoginEffect.NothingEffect
                    }
                }
            }
            is LoginAction.RegisterUserAction -> {
                addIntermediateEffect(LoginEffect.AuthProceedingEffect)
                when (val result = authUseCase.registerUser(
                    action.username,
                    action.password,
                    action.firstName,
                    action.lastName,
                    action.isTeacher
                )) {
                    is Result.Error -> LoginEffect.AuthProceedingErrorEffect(result.throwable)
                    is Result.Success -> {
                        addIntermediateEffect(LoginEffect.UserRegisteredEffect)
                        delay(3000L)
                        LoginEffect.HideRegisteredMessageEffect
                    }
                }
            }
        }

    override fun stateReducer(oldState: LoginViewState, effect: LoginEffect): LoginViewState =
        when (effect) {
            LoginEffect.AuthProceedingEffect -> LoginViewState(
                isLoading = true,
                isLogin = oldState.isLogin,
                error = null,
                isRegistered = oldState.isRegistered
            )
            is LoginEffect.AuthProceedingErrorEffect -> LoginViewState(
                isLoading = false,
                isLogin = oldState.isLogin,
                error = effect.error,
                isRegistered = oldState.isRegistered
            )
            is LoginEffect.ChangeStateEffect -> LoginViewState(
                isLoading = false,
                isLogin = effect.isLogin,
                error = null,
                isRegistered = oldState.isRegistered
            )
            LoginEffect.NothingEffect -> oldState
            LoginEffect.HideRegisteredMessageEffect -> LoginViewState(
                isLoading = oldState.isLoading,
                isLogin = oldState.isLogin,
                error = oldState.error,
                isRegistered = false
            )
            LoginEffect.UserRegisteredEffect -> LoginViewState(
                isLoading = false,
                isLogin = oldState.isLogin,
                error = null,
                isRegistered = true
            )
        }

}