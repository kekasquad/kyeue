package io.kekasquad.queue.queue

import androidx.hilt.lifecycle.ViewModelInject
import io.kekasquad.queue.base.BaseViewModel
import io.kekasquad.queue.data.usecase.QueueUseCase
import io.kekasquad.queue.nav.Coordinator
import io.kekasquad.queue.vo.inapp.Result

class QueueViewModel @ViewModelInject constructor(
    private val coordinator: Coordinator,
    private val queueUseCase: QueueUseCase
) : BaseViewModel<QueueViewState, QueueEffect, QueueIntent, QueueAction>() {
    override fun initialState(): QueueViewState = QueueViewState.initialState

    override fun intentInterpreter(intent: QueueIntent): QueueAction =
        when (intent) {
            QueueIntent.ArrowBackClickIntent -> QueueAction.NavigateBackAction
            is QueueIntent.InitialIntent -> QueueAction.InitialLoadingAction(intent.id)
            is QueueIntent.RetryInitialIntent -> QueueAction.InitialLoadingAction(intent.id)
            QueueIntent.QueueNothingIntent -> throw IllegalArgumentException("nothing intent interpreting")
        }

    override suspend fun performAction(action: QueueAction): QueueEffect =
        when (action) {
            is QueueAction.InitialLoadingAction -> {
                addIntermediateEffect(QueueEffect.InitialLoadingEffect)
                when (val result = queueUseCase.getQueueById(action.id, null)) {
                    is Result.Error -> QueueEffect.InitialLoadingErrorEffect(result.throwable)
                    is Result.Success -> QueueEffect.DataLoadedEffect(result.data.members)
                }
            }
            QueueAction.NavigateBackAction -> {
                coordinator.pop()
                QueueEffect.NothingEffect
            }
        }

    override fun stateReducer(oldState: QueueViewState, effect: QueueEffect): QueueViewState =
        when (effect) {
            is QueueEffect.DataLoadedEffect -> QueueViewState.dataLoadedState(effect.data)
            QueueEffect.InitialLoadingEffect -> QueueViewState.initialLoadingState
            is QueueEffect.InitialLoadingErrorEffect -> QueueViewState.initialErrorState(effect.throwable)
            QueueEffect.NothingEffect -> oldState
        }

}