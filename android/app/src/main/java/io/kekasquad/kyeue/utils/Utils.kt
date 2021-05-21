package io.kekasquad.kyeue.utils

import io.kekasquad.kyeue.BuildConfig

private val ALPHABET = ('a'..'z') + ('A'..'Z')

fun generateRandomString(length: Int): String {
    if (!BuildConfig.DEBUG) {
        throw IllegalAccessError("generating models not in debug");
    }
    return List(length) { ALPHABET.random() }.joinToString("")
}