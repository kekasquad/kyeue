package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json

data class PopMemberMessage(
    @Json(name = "pop_member") val memberId: String
)