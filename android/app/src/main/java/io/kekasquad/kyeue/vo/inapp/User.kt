package io.kekasquad.kyeue.vo.inapp

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val isTeacher: Boolean
)