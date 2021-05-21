package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json

data class DeleteQueueMessage(
    @Json(name = "delete_queue") val queueId: String
)