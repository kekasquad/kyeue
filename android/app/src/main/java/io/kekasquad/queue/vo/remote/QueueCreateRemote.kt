package io.kekasquad.queue.vo.remote

data class QueueCreateRemote(
    val id: String,
    val name: String,
    val creator: UserRemote
)