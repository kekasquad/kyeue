package io.kekasquad.kyeue.ui.queue

import io.kekasquad.kyeue.base.MviEffect
import io.kekasquad.kyeue.vo.inapp.Queue

sealed class QueueEffect : MviEffect {

    object InitialLoadingEffect : QueueEffect()

    data class InitialLoadingErrorEffect(
        val message: Int
    ) : QueueEffect()

    data class DataLoadedEffect(
        val data: List<Queue>
    ) : QueueEffect()

    object PagingLoadingEffect : QueueEffect()

    data class PagingLoadingErrorEffect(
        val message: Int
    ) : QueueEffect()

    object CreateDialogEffect : QueueEffect()

    data class RenameDialogEffect(
        val queueToRename: Queue
    ) : QueueEffect()

    data class DeleteDialogEffect(
        val queueToDelete: Queue
    ) : QueueEffect()

    object DismissEffect : QueueEffect()

    data class NameChangedEffect(
        val queueName: String
    ) : QueueEffect()

    data class NameErrorEffect(
        val error: Int
    ) : QueueEffect()

    object NoEffect : QueueEffect()

    object QueueActionPerformingEffect : QueueEffect()

    data class QueueActionMessageEffect(
        val message: Int
    ) : QueueEffect()

    object DismissMessageEffect : QueueEffect()

    data class DeleteQueueEffect(
        val queueId: String
    ) : QueueEffect()

    data class AddQueueEffect(
        val queue: Queue
    ) : QueueEffect()

    data class AddQueueErrorEffect(
        val message: Int
    ) : QueueEffect()

    data class RenameQueueEffect(
        val queue: Queue
    ) : QueueEffect()

    data class RenameQueueErrorEffect(
        val message: Int
    ) : QueueEffect()

}
