package io.kekasquad.kyeue.vo.inapp

sealed class QueueDetailsMessage(val userId: String) {

    class UserEnteredMessage(
        userId: String
    ) : QueueDetailsMessage(userId = userId)

    class UserLeftMessage(
        userId: String
    ) : QueueDetailsMessage(userId = userId)

    class UserSkippedTurnMessage(
        userId: String
    ) : QueueDetailsMessage(userId = userId)

    class UserMovedToTheEndMessage(
        userId: String
    ) : QueueDetailsMessage(userId = userId)

}