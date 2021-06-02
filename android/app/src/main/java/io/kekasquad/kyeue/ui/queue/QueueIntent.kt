package io.kekasquad.kyeue.ui.queue

import io.kekasquad.kyeue.base.MviIntent
import io.kekasquad.kyeue.base.NothingIntent
import io.kekasquad.kyeue.vo.inapp.Queue

sealed class QueueIntent : MviIntent {

    object InitialLoadingIntent : QueueIntent()

    object QueueNothingIntent : QueueIntent(), NothingIntent

    object RetryInitialLoadingIntent : QueueIntent()

    object PagingLoadingIntent : QueueIntent()

    object RetryPagingLoadingIntent : QueueIntent()

    object OpenCreateDialogIntent : QueueIntent()

    data class OpenRenameDialogIntent(
        val queueToRename: Queue
    ) : QueueIntent()

    data class OpenDeleteDialogIntent(
        val queueToDelete: Queue
    ) : QueueIntent()

    object DismissCreateDialogIntent : QueueIntent()

    object DismissRenameDialogIntent : QueueIntent()

    object DismissDeleteDialogIntent : QueueIntent()

    data class InputQueueNameIntent(
        val queueName: String
    ) : QueueIntent()

    object CreateQueueIntent : QueueIntent()

    object RenameQueueIntent : QueueIntent()

    object DeleteQueueIntent : QueueIntent()

    data class OpenQueueDetailsIntent(
        val queue: Queue
    ) : QueueIntent()

    object LogoutIntent : QueueIntent()

}
