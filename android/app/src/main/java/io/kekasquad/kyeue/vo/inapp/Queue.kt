package io.kekasquad.kyeue.vo.inapp

import androidx.compose.runtime.Immutable
import io.kekasquad.kyeue.BuildConfig
import io.kekasquad.kyeue.utils.generateRandomString

@Immutable
data class Queue(
    val id: String,
    val name: String,
    val isPrivate: Boolean,
    val creator: User,
    val members: List<User>
) {
    companion object {
        fun generate(creator: User): Queue {
            if (!BuildConfig.DEBUG) {
                throw IllegalAccessError("generating models not in debug");
            }
            return Queue(
                id = generateRandomString(12),
                name = generateRandomString(12),
                isPrivate = false,
                creator = creator,
                members = List(12) { User.generate() }
            )
        }
    }
}