package io.kekasquad.queue.vo.inapp

data class Queue(
    val id: String,
    val name: String,
    val isPrivate: Boolean,
    val creator: User,
    val members: List<User>
)