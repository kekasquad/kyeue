package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MessageWrapper(
    val type: String,
    val text: Message
)

data class Message(
    @Json(name = "create_queue") val createdQueueId: String?,
    @Json(name = "rename_queue") val renamedQueueId: String?,
    @Json(name = "delete_queue") val deletedQueueId: String?
)

