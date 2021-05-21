package io.kekasquad.kyeue.vo.remote

data class QueueCreateRemote(
    val id: String,
    val name: String,
    val creator: UserRemote
)