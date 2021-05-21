package io.kekasquad.kyeue.utils

import io.kekasquad.kyeue.vo.inapp.Result

inline fun <T> safeApiCall(block: () -> Result<T>): Result<T> =
    try {
        block()
    } catch (e: Throwable) {
        Result.Error(e)
    }