package io.kekasquad.queue.queues

import io.kekasquad.queue.base.MviIntent
import io.kekasquad.queue.base.NothingIntent

sealed class QueuesIntent : MviIntent {

    object InitialIntent : QueuesIntent()

    object RetryInitialIntent : QueuesIntent()

    object PagingLoadingIntent : QueuesIntent()

    object PagingRetryLoadingIntent : QueuesIntent()

    object QueuesNothingIntent : QueuesIntent(), NothingIntent

}