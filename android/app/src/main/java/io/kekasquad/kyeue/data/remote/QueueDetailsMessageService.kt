package io.kekasquad.kyeue.data.remote

import com.tinder.scarlet.ws.Receive
import io.kekasquad.kyeue.vo.remote.MoveMemberToTheEndMessage
import io.kekasquad.kyeue.vo.remote.PopMemberMessage
import io.kekasquad.kyeue.vo.remote.PushMemberMessage
import io.kekasquad.kyeue.vo.remote.SkipTurnMessage
import kotlinx.coroutines.channels.ReceiveChannel

interface QueueDetailsMessageService {

    @Receive
    fun observeMemberPushing(): ReceiveChannel<PushMemberMessage>

    @Receive
    fun observeMemberDeleting(): ReceiveChannel<PopMemberMessage>

    @Receive
    fun observeSkipTurnEvent(): ReceiveChannel<SkipTurnMessage>

    @Receive
    fun observeMoveToTheEndEvent(): ReceiveChannel<MoveMemberToTheEndMessage>

}