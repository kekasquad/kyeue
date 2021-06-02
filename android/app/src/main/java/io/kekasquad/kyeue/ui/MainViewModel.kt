package io.kekasquad.kyeue.ui

import dagger.hilt.android.lifecycle.HiltViewModel
import io.kekasquad.kyeue.base.BaseViewModel
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
) : BaseViewModel<MainActivityViewState, MainActivityEffect, MainActivityIntent, MainActivityAction, MainActivityNavigationEvent>() {
    override fun initialState(): MainActivityViewState = MainActivityViewState

    override fun intentInterpreter(intent: MainActivityIntent): MainActivityAction =
        when (intent) {
            MainActivityIntent.InitialIntent -> MainActivityAction.IsUserLoggedInAction
            MainActivityIntent.MainActivityNothingIntent -> throw IllegalStateException("nothing intent interpreting")
        }

    override suspend fun performAction(action: MainActivityAction): MainActivityEffect =
        when (action) {
            MainActivityAction.IsUserLoggedInAction -> {
                if (authUseCase.isTokenExists()) {
                    navigationEventLiveData.postValue(MainActivityNavigationEvent.NavigateToQueueEvent)
                } else {
                    navigationEventLiveData.postValue(MainActivityNavigationEvent.NavigateToLoginEvent)
                }
                MainActivityEffect.NoEffect
            }
        }

    override fun stateReducer(
        oldState: MainActivityViewState,
        effect: MainActivityEffect
    ): MainActivityViewState =
        when (effect) {
            MainActivityEffect.NoEffect -> oldState
        }
}