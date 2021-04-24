package io.kekasquad.queue.vo.remote

data class QueueRemote(
    val id: String,
    val name: String,
    val isPrivate: Boolean,
    val creator: UserRemote,
    val members: List<UserRemote>,
)