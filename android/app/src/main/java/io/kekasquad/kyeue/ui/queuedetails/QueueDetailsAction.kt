package io.kekasquad.kyeue.ui.queuedetails

import io.kekasquad.kyeue.base.MviAction
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.User

sealed class QueueDetailsAction : MviAction {

    data class LoadQueueDetailsAction(
        val queue: Queue?
    ) : QueueDetailsAction()

    object NavigateBackAction : QueueDetailsAction()

    object EnterQueueAction : QueueDetailsAction()

    data class UserLeaveFromQueueAction(
        val user: User
    ) : QueueDetailsAction()

    data class UserSkipTurnAction(
        val user: User
    ) : QueueDetailsAction()

    data class UserToTheEndAction(
        val user: User
    ) : QueueDetailsAction()

}

