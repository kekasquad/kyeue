package io.kekasquad.kyeue.vo.inapp

import androidx.compose.runtime.Immutable

@Immutable
data class QueuePage(
    val nextOffset: Int?,
    val queues: List<Queue>
)