package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json

data class PushMemberMessage(
    @Json(name = "push_member") val memberId: String
)