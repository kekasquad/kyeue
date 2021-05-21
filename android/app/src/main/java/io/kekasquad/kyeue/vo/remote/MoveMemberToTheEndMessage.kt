package io.kekasquad.kyeue.vo.remote

import com.squareup.moshi.Json

data class MoveMemberToTheEndMessage(
    @Json(name = "move_member_to_the_end") val memberId: String
)