package io.kekasquad.kyeue.ui.queue

import io.kekasquad.kyeue.base.MviAction
import io.kekasquad.kyeue.vo.inapp.Queue

sealed class QueueAction : MviAction {

    object InitialLoadingAction : QueueAction()

    object PagingLoadingAction : QueueAction()

    object CreateQueueAction : QueueAction()

    object RenameQueueAction : QueueAction()

    object DeleteQueueAction : QueueAction()

    object OpenCreateQueueDialogAction : QueueAction()

    data class OpenRenameQueueDialogAction(
        val queueToRename: Queue
    ) : QueueAction()

    data class OpenDeleteQueueDialogAction(
        val queueToDelete: Queue
    ) : QueueAction()

    object DismissDialogsAction : QueueAction()

    data class ChangeQueueNameAction(
        val queueName: String
    ) : QueueAction()

    data class NavigateToQueueDetailsAction(
        val queue: Queue
    ) : QueueAction()

}
