package io.kekasquad.kyeue.vo.inapp

import androidx.compose.runtime.Immutable
import io.kekasquad.kyeue.BuildConfig
import io.kekasquad.kyeue.utils.generateRandomString

@Immutable
data class User(
    val id: String,
    val username: String,
    val firstName: String,
    val lastName: String,
    val isTeacher: Boolean
) {
    companion object {
        fun generate(): User {
            if (!BuildConfig.DEBUG) {
                throw IllegalAccessError("generating models not in debug");
            }
            return User(
                id = generateRandomString(12),
                username = generateRandomString(12),
                firstName = generateRandomString(12),
                lastName = generateRandomString(12),
                isTeacher = false
            )
        }
    }
}