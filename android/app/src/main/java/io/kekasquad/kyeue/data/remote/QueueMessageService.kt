package io.kekasquad.kyeue.data.remote

import com.tinder.scarlet.ws.Receive
import io.kekasquad.kyeue.vo.remote.*
import kotlinx.coroutines.channels.ReceiveChannel

interface QueueMessageService {

    @Receive
    fun observeQueueCreation(): ReceiveChannel<CreateQueueMessage>

    @Receive
    fun observeQueueDeletion(): ReceiveChannel<DeleteQueueMessage>

}