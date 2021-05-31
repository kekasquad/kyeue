package io.kekasquad.kyeue.data.remote

import com.squareup.moshi.JsonAdapter
import io.kekasquad.kyeue.vo.remote.MessageWrapper
import io.kekasquad.kyeue.vo.remote.RemoteQueueDetailsMessage
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

interface QueueDetailsMessageService {
    fun getQueueDetailsMessageFlow(queueId: String): Flow<RemoteQueueDetailsMessage>
}

class QueueDetailsMessageServiceImpl @Inject constructor(
    private val httpClient: OkHttpClient,
    private val messageAdapter: JsonAdapter<MessageWrapper>
) : QueueDetailsMessageService {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getQueueDetailsMessageFlow(
        queueId: String
    ): Flow<RemoteQueueDetailsMessage> = callbackFlow {
        val socketListener = object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                val messageWrapper = messageAdapter.fromJson(text) ?: return
                val message = when {
                    messageWrapper.text.addedUserId != null ->
                        RemoteQueueDetailsMessage.UserEnteredMessage(messageWrapper.text.addedUserId)
                    messageWrapper.text.leftUserId != null ->
                        RemoteQueueDetailsMessage.UserLeftMessage(messageWrapper.text.leftUserId)
                    messageWrapper.text.skippedTunUserId != null ->
                        RemoteQueueDetailsMessage.UserSkippedTurnMessage(messageWrapper.text.skippedTunUserId)
                    messageWrapper.text.movedToTheEndUserId != null ->
                        RemoteQueueDetailsMessage.UserMovedToTheEndMessage(messageWrapper.text.movedToTheEndUserId)
                    else -> return
                }
                sendBlocking(message)
            }
        }

        val request = Request.Builder()
            .url("ws://192.168.43.103:8080/ws/queue/$queueId/")
            .build()

        // Set the listener to the socket
        val socket = httpClient.newWebSocket(request, socketListener)
        awaitClose { socket.cancel() }
    }

}