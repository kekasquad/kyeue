package io.kekasquad.queue.login

import io.kekasquad.queue.base.MviViewState

data class LoginViewState(
    val isLoading: Boolean = false,
    val isLogin: Boolean = true,
    val error: Throwable? = null,
    val isRegistered: Boolean = false
) : MviViewState {
    override fun log(): String = this.toString()
}