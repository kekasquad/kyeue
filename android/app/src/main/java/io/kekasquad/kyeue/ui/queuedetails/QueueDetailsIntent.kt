package io.kekasquad.kyeue.ui.queuedetails

import io.kekasquad.kyeue.base.MviIntent
import io.kekasquad.kyeue.base.NothingIntent
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.User

sealed class QueueDetailsIntent : MviIntent {

    data class InitialIntent(
        val queueId: String,
        val queue: Queue?
    ) : QueueDetailsIntent()

    object QueueDetailsNothingIntent : QueueDetailsIntent(), NothingIntent

    object RetryInitialLoadingIntent : QueueDetailsIntent()

    object BackButtonClickIntent : QueueDetailsIntent()

    object EnterQueueIntent : QueueDetailsIntent()

    data class LeaveUserIntent(
        val user: User
    ) : QueueDetailsIntent()

    data class UserSkipTurnIntent(
        val user: User
    ) : QueueDetailsIntent()

    data class UserToTheEndIntent(
        val user: User
    ) : QueueDetailsIntent()

}