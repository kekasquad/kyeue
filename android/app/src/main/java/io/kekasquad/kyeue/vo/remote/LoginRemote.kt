package io.kekasquad.kyeue.vo.remote

data class LoginRemote(
    val key: String,
    val user: UserRemote,
    val created: String
)