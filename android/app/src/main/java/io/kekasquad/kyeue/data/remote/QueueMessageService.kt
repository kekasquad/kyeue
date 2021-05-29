package io.kekasquad.kyeue.data.remote

import com.squareup.moshi.JsonAdapter
import io.kekasquad.kyeue.vo.remote.MessageWrapper
import io.kekasquad.kyeue.vo.remote.RemoteQueueMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

interface QueueMessageService {
    fun getQueueMessagesFlow(): Flow<RemoteQueueMessage>
}

class QueueMessageServiceImpl @Inject constructor(
    private val httpClient: OkHttpClient,
    private val messageAdapter: JsonAdapter<MessageWrapper>
) : QueueMessageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getQueueMessagesFlow(): Flow<RemoteQueueMessage> = callbackFlow {
        val socketListener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val messageWrapper = messageAdapter.fromJson(text) ?: return
                val message = when {
                    messageWrapper.text.createdQueueId != null ->
                        RemoteQueueMessage.CreateQueueMessage(messageWrapper.text.createdQueueId)
                    messageWrapper.text.deletedQueueId != null ->
                        RemoteQueueMessage.DeleteQueueMessage(messageWrapper.text.deletedQueueId)
                    messageWrapper.text.renamedQueueId != null ->
                        RemoteQueueMessage.RenameQueueMessage(messageWrapper.text.renamedQueueId)
                    else -> return
                }
                sendBlocking(message)
            }
        }

        val request = Request.Builder()
            .url("ws://192.168.43.103:8080/ws/notifications/")
            .build()

        // Set the listener to the socket
        val socket = httpClient.newWebSocket(request, socketListener)
        awaitClose { socket.cancel() }
    }


}