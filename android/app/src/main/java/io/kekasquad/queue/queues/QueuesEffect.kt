package io.kekasquad.queue.queues

import io.kekasquad.queue.base.MviEffect
import io.kekasquad.queue.vo.Queue

sealed class QueuesEffect : MviEffect {

    object NothingEffect : QueuesEffect()

    object InitialLoadingEffect : QueuesEffect()

    data class InitialLoadingErrorEffect(
        val throwable: Throwable
    ) : QueuesEffect()

    object PagingLoadingEffect : QueuesEffect()

    data class PagingLoadingErrorEffect(
        val throwable: Throwable
    ) : QueuesEffect()

    data class DataLoadedEffect(
        val data: List<Queue>
    ) : QueuesEffect()

}