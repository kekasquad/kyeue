package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json

data class SkipTurnMessage(
    @Json(name = "skip_turn") val memberId: String
)