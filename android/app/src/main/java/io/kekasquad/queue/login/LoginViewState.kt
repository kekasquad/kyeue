package io.kekasquad.queue.login

import io.kekasquad.queue.base.MviViewState

data class LoginViewState(
    val username: String = "",
    val password: String = "",
    val name: String = "",
    val group: String = "",
    val isLogin: Boolean = true,
    val usernameError: String? = null,
    val passwordError: String? = null
) : MviViewState {
    override fun log(): String = this.toString()
}