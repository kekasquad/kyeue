package io.kekasquad.queue.vo.remote

import io.kekasquad.queue.vo.inapp.User

data class LoginRemote(
    val key: String,
    val user: User,
    val created: String
)