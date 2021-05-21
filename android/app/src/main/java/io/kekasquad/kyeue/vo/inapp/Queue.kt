package io.kekasquad.kyeue.vo.inapp

import androidx.compose.runtime.Immutable

@Immutable
data class Queue(
    val id: String,
    val name: String,
    val isPrivate: Boolean,
    val creator: User,
    val members: List<User>
)