package io.kekasquad.queue.queue

import io.kekasquad.queue.base.MviIntent
import io.kekasquad.queue.base.NothingIntent

sealed class QueueIntent : MviIntent {

    data class InitialIntent(
        val id: String
    ) : QueueIntent()

    data class RetryInitialIntent(
        val id: String
    ) : QueueIntent()

    data class PagingLoadingIntent(
        val id: String
    ) : QueueIntent()

    data class PagingRetryLoadingIntent(
        val id: String
    ) : QueueIntent()

    object ArrowBackClickIntent : QueueIntent()

    object QueueNothingIntent : QueueIntent(), NothingIntent

}