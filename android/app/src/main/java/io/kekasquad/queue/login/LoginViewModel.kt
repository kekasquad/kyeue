package io.kekasquad.queue.login

import androidx.hilt.lifecycle.ViewModelInject
import io.kekasquad.queue.base.BaseViewModel
import io.kekasquad.queue.nav.Coordinator

class LoginViewModel @ViewModelInject constructor(
    private val coordinator: Coordinator
) : BaseViewModel<LoginViewState, LoginEffect, LoginIntent, LoginAction>() {
    override fun initialState(): LoginViewState = LoginViewState()

    override fun intentInterpreter(intent: LoginIntent): LoginAction {
        TODO("Not yet implemented")
    }

    override suspend fun performAction(action: LoginAction): LoginEffect {
        TODO("Not yet implemented")
    }

    override fun stateReducer(oldState: LoginViewState, effect: LoginEffect): LoginViewState {
        TODO("Not yet implemented")
    }
}