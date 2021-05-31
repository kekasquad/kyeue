package io.kekasquad.kyeue.ui.queuedetails

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.base.BaseViewModel
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import io.kekasquad.kyeue.data.usecase.QueueDetailsUseCase
import io.kekasquad.kyeue.vo.inapp.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QueueDetailsViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val queueDetailsUseCase: QueueDetailsUseCase
) : BaseViewModel<QueueDetailsViewState, QueueDetailsEffect, QueueDetailsIntent, QueueDetailsAction, QueueDetailsNavigationEvent>() {
    private lateinit var queueId: String

    override fun initialState(): QueueDetailsViewState = QueueDetailsViewState.initialState(
        currentUser = authUseCase.getCurrentUser()
    )

    private fun startListening() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                queueDetailsUseCase.queueDetailsMessageFlow(queueId)
                    .collect {
                        val effect = when (val result =
                            queueDetailsUseCase.getQueueById(queueId = queueId)) {
                            is Result.Error -> QueueDetailsEffect.MessageEffect(R.string.error_member_loading_error_message)
                            is Result.Success -> QueueDetailsEffect.LoadedStateEffect(result.data)
                        }
                        addIntermediateEffect(effect)
                    }
            }
        }
    }

    override fun intentInterpreter(intent: QueueDetailsIntent): QueueDetailsAction =
        when (intent) {
            QueueDetailsIntent.BackButtonClickIntent -> QueueDetailsAction.NavigateBackAction
            QueueDetailsIntent.EnterQueueIntent -> QueueDetailsAction.EnterQueueAction
            is QueueDetailsIntent.InitialIntent -> QueueDetailsAction.LoadQueueDetailsAction(
                queue = intent.queue
            ).also { queueId = intent.queueId }
            is QueueDetailsIntent.LeaveUserIntent -> QueueDetailsAction.UserLeaveFromQueueAction(
                user = intent.user
            )
            QueueDetailsIntent.QueueDetailsNothingIntent -> throw IllegalStateException("nothing intent interpretation")
            QueueDetailsIntent.RetryInitialLoadingIntent -> QueueDetailsAction.LoadQueueDetailsAction(
                queue = null
            )
            is QueueDetailsIntent.UserSkipTurnIntent -> QueueDetailsAction.UserSkipTurnAction(
                user = intent.user
            )
            is QueueDetailsIntent.UserToTheEndIntent -> QueueDetailsAction.UserToTheEndAction(
                user = intent.user
            )
        }

    override suspend fun performAction(action: QueueDetailsAction): QueueDetailsEffect =
        when (action) {
            QueueDetailsAction.EnterQueueAction -> {
                when (val result = queueDetailsUseCase.enterQueue(
                    queueId = queueId,
                    userId = viewStateLiveData.value!!.currentUser.id
                )) {
                    is Result.Error -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.error_member_loading_error_message))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                    is Result.Success -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.message_member_added_success))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                }
            }
            is QueueDetailsAction.LoadQueueDetailsAction -> {
                if (action.queue != null) {
                    addIntermediateEffect(QueueDetailsEffect.InitialLoadingEffect)
                } else {
                    addIntermediateEffect(QueueDetailsEffect.InitialLoadingEffect)
                }
                when (val result = queueDetailsUseCase.getQueueById(queueId = queueId)) {
                    is Result.Error -> QueueDetailsEffect.InitialLoadingErrorEffect(R.string.error_member_loading_error_message)
                    is Result.Success -> QueueDetailsEffect.LoadedStateEffect(result.data).also {
                        startListening()
                    }
                }
            }
            QueueDetailsAction.NavigateBackAction -> {
                navigationEventLiveData.postValue(QueueDetailsNavigationEvent.NavigateBackEvent)
                QueueDetailsEffect.NoEffect
            }
            is QueueDetailsAction.UserLeaveFromQueueAction -> {
                when (val result = queueDetailsUseCase.leaveQueue(
                    queueId = queueId,
                    userId = action.user.id
                )) {
                    is Result.Error -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.error_member_loading_error_message))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                    is Result.Success -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.message_member_leave_success))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                }
            }
            is QueueDetailsAction.UserSkipTurnAction -> {
                when (val result = queueDetailsUseCase.skipTurnInQueue(
                    queueId = queueId,
                    userId = action.user.id
                )) {
                    is Result.Error -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.message_member_skip_turn_success))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                    is Result.Success -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.message_member_leave_success))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                }
            }
            is QueueDetailsAction.UserToTheEndAction -> {
                when (val result = queueDetailsUseCase.moveToEndInQueue(
                    queueId = queueId,
                    userId = action.user.id
                )) {
                    is Result.Error -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.message_member_to_the_end_success))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                    is Result.Success -> {
                        addIntermediateEffect(QueueDetailsEffect.MessageEffect(R.string.message_member_leave_success))
                        delay(3000L)
                        QueueDetailsEffect.DismissMessageEffect
                    }
                }
            }
        }

    override fun stateReducer(
        oldState: QueueDetailsViewState,
        effect: QueueDetailsEffect
    ): QueueDetailsViewState =
        when (effect) {
            QueueDetailsEffect.DismissMessageEffect -> oldState.dismissMessageState()
            QueueDetailsEffect.InitialLoadingEffect -> oldState.loadingState()
            is QueueDetailsEffect.InitialLoadingErrorEffect -> oldState.initialErrorState(effect.initialErrorMessage)
            is QueueDetailsEffect.LoadedStateEffect -> oldState.loadedState(effect.queue)
            is QueueDetailsEffect.MessageEffect -> oldState.messageState(effect.message)
            QueueDetailsEffect.NoEffect -> oldState
        }

}