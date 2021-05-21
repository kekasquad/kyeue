package io.kekasquad.kyeue.data.remote

import com.tinder.scarlet.ws.Receive
import io.kekasquad.kyeue.vo.remote.*
import kotlinx.coroutines.channels.ReceiveChannel

interface MessageApi {

    @Receive
    fun observeQueueCreation(): ReceiveChannel<CreateQueueMessage>

    @Receive
    fun observeQueueDeletion(): ReceiveChannel<DeleteQueueMessage>

    @Receive
    fun observeMemberPushing(): ReceiveChannel<PushMemberMessage>

    @Receive
    fun observeMemberDeleting(): ReceiveChannel<PopMemberMessage>

    @Receive
    fun observeSkipTurnEvent(): ReceiveChannel<SkipTurnMessage>

    @Receive
    fun observeMoveToTheEndEvent(): ReceiveChannel<MoveMemberToTheEndMessage>

}