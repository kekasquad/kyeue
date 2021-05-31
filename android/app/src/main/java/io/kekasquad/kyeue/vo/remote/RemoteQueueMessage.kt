package io.kekasquad.kyeue.vo.remote

sealed class RemoteQueueMessage {

    data class CreateQueueMessage(
        val createdId: String
    ) : RemoteQueueMessage()

    data class RenameQueueMessage(
        val renamedId: String
    ) : RemoteQueueMessage()

    data class DeleteQueueMessage(
        val deletedId: String
    ) : RemoteQueueMessage()

}