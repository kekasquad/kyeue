package io.kekasquad.kyeue.vo.remote

data class UserRemote(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val isTeacher: Boolean
)