package io.kekasquad.queue.queues

import androidx.hilt.lifecycle.ViewModelInject
import io.kekasquad.queue.base.BaseViewModel
import io.kekasquad.queue.data.usecase.QueueUseCase
import io.kekasquad.queue.nav.Coordinator
import io.kekasquad.queue.vo.inapp.Result

class QueuesViewModel @ViewModelInject constructor(
    private val coordinator: Coordinator,
    private val queueUseCase: QueueUseCase
) : BaseViewModel<QueuesViewState, QueuesEffect, QueuesIntent, QueuesAction>() {
    override fun initialState(): QueuesViewState = QueuesViewState.initialState

    override fun intentInterpreter(intent: QueuesIntent): QueuesAction =
        when (intent) {
            QueuesIntent.InitialIntent -> QueuesAction.InitialLoadingAction
            QueuesIntent.PagingLoadingIntent -> QueuesAction.PagingLoadingAction
            QueuesIntent.PagingRetryLoadingIntent -> QueuesAction.PagingLoadingAction
            QueuesIntent.QueuesNothingIntent -> throw IllegalArgumentException("nothing intent interpreting")
            QueuesIntent.RetryInitialIntent -> QueuesAction.InitialLoadingAction
        }

    override suspend fun performAction(action: QueuesAction): QueuesEffect =
        when (action) {
            QueuesAction.InitialLoadingAction -> {
                addIntermediateEffect(QueuesEffect.InitialLoadingEffect)
                when (val result = queueUseCase.getQueues(null)) {
                    is Result.Error -> QueuesEffect.InitialLoadingErrorEffect(result.throwable)
                    is Result.Success -> QueuesEffect.DataLoadedEffect(result.data)
                }
            }
            QueuesAction.PagingLoadingAction -> {
                addIntermediateEffect(QueuesEffect.PagingLoadingEffect)
                when (val result = queueUseCase.getQueues(null)) { //TODO: KAPPA
                    is Result.Error -> QueuesEffect.PagingLoadingErrorEffect(result.throwable)
                    is Result.Success -> QueuesEffect.DataLoadedEffect(result.data)
                }
            }
        }

    override fun stateReducer(oldState: QueuesViewState, effect: QueuesEffect): QueuesViewState =
        when (effect) {
            is QueuesEffect.DataLoadedEffect -> QueuesViewState.dataLoadedState(effect.data)
            QueuesEffect.InitialLoadingEffect -> QueuesViewState.initialLoadingState
            is QueuesEffect.InitialLoadingErrorEffect -> QueuesViewState.initialErrorState(effect.throwable)
            QueuesEffect.NothingEffect -> oldState
            QueuesEffect.PagingLoadingEffect -> QueuesViewState.pagingLoadingState(oldState.data)
            is QueuesEffect.PagingLoadingErrorEffect -> QueuesViewState.pagingLoadingErrorState(
                oldState.data,
                effect.throwable
            )
        }
}