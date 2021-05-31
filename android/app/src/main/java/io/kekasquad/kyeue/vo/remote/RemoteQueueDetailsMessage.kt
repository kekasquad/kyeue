package io.kekasquad.kyeue.vo.remote

sealed class RemoteQueueDetailsMessage {

    class UserEnteredMessage(
        val userId: String
    ) : RemoteQueueDetailsMessage()

    class UserLeftMessage(
        val userId: String
    ) : RemoteQueueDetailsMessage()

    class UserSkippedTurnMessage(
        val userId: String
    ) : RemoteQueueDetailsMessage()

    class UserMovedToTheEndMessage(
        val userId: String
    ) : RemoteQueueDetailsMessage()

}