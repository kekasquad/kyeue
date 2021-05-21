package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json

data class CreateQueueMessage(
    @Json(name = "create_queue") val queueId: String
)