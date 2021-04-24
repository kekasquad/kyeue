package io.kekasquad.queue.vo.inapp

data class QueuePage(
    val nextOffset: Int?,
    val queues: List<Queue>
)