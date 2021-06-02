package io.kekasquad.kyeue.ui

import io.kekasquad.kyeue.base.MviEffect

sealed class MainActivityEffect : MviEffect {

    object NoEffect : MainActivityEffect()

}