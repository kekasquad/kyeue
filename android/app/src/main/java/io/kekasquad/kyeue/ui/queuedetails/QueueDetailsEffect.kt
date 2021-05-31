package io.kekasquad.kyeue.ui.queuedetails

import io.kekasquad.kyeue.base.MviEffect
import io.kekasquad.kyeue.vo.inapp.Queue

sealed class QueueDetailsEffect : MviEffect {

    object InitialLoadingEffect : QueueDetailsEffect()

    data class InitialLoadingErrorEffect(
        val initialErrorMessage: Int
    ) : QueueDetailsEffect()

    data class LoadedStateEffect(
        val queue: Queue
    ) : QueueDetailsEffect()

    data class MessageEffect(
        val message: Int
    ) : QueueDetailsEffect()

    object DismissMessageEffect : QueueDetailsEffect()

    object NoEffect : QueueDetailsEffect()

}