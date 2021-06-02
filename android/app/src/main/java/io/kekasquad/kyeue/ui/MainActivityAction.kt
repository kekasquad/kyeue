package io.kekasquad.kyeue.ui

import io.kekasquad.kyeue.base.MviAction

sealed class MainActivityAction : MviAction {

    object IsUserLoggedInAction : MainActivityAction()

}
