package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

sealed class RemoteQueueMessage {

    @JsonClass(generateAdapter = true)
    data class CreateQueueMessage(
        @Json(name = "create_queue") val createdId: String
    ) : RemoteQueueMessage()

    @JsonClass(generateAdapter = true)
    data class RenameQueueMessage(
        @Json(name = "rename_queue") val renamedId: String
    ) : RemoteQueueMessage()

    @JsonClass(generateAdapter = true)
    data class DeleteQueueMessage(
        @Json(name = "delete_queue") val deletedId: String
    ) : RemoteQueueMessage()

}