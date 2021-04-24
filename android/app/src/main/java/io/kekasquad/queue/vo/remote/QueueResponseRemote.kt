package io.kekasquad.queue.vo.remote

data class QueueResponseRemote(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<QueueRemote>
)