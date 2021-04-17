package io.kekasquad.queue.queue

import io.kekasquad.queue.base.MviEffect
import io.kekasquad.queue.vo.User

sealed class QueueEffect : MviEffect {

    object NothingEffect : QueueEffect()

    object InitialLoadingEffect : QueueEffect()

    data class InitialLoadingErrorEffect(
        val throwable: Throwable
    ) : QueueEffect()

    object PagingLoadingEffect : QueueEffect()

    data class PagingLoadingErrorEffect(
        val throwable: Throwable
    ) : QueueEffect()

    data class DataLoadedEffect(
        val data: List<User>
    ) : QueueEffect()

}