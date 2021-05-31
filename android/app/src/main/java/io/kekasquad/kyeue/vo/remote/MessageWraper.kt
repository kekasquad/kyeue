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
    @Json(name = "delete_queue") val deletedQueueId: String?,
    @Json(name = "push_member") val addedUserId: String?,
    @Json(name = "pop_member") val leftUserId: String?,
    @Json(name = "skip_turn") val skippedTunUserId: String?,
    @Json(name = "move_member_to_the_end") val movedToTheEndUserId: String?
)

