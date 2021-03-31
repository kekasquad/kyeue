package io.kekasquad.queue.vo

data class Queue(
    val id: String,
    val name: String,
    val isPrivate: Boolean,
    val members: List<User>
)