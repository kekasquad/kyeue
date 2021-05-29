package io.kekasquad.kyeue.vo.inapp

sealed class QueueMessage(val queueId: String) {

    class CreateQueueMessage(
        queueId: String
    ) : QueueMessage(queueId = queueId)

    class RenameQueueMessage(
        queueId: String
    ) : QueueMessage(queueId = queueId)

    class DeleteQueueMessage(
        queueId: String
    ) : QueueMessage(queueId = queueId)

}