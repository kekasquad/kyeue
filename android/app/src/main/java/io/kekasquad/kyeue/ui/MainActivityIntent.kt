package io.kekasquad.kyeue.ui

import io.kekasquad.kyeue.base.MviIntent
import io.kekasquad.kyeue.base.NothingIntent

sealed class MainActivityIntent : MviIntent {

    object InitialIntent : MainActivityIntent()

    object MainActivityNothingIntent : MainActivityIntent(), NothingIntent

}