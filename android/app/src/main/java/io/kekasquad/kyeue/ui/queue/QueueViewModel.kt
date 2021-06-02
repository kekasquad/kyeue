package io.kekasquad.kyeue.ui.queue

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.base.BaseViewModel
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import io.kekasquad.kyeue.data.usecase.QueueDetailsUseCase
import io.kekasquad.kyeue.data.usecase.QueueUseCase
import io.kekasquad.kyeue.vo.inapp.QueueMessage
import io.kekasquad.kyeue.vo.inapp.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val queueUseCase: QueueUseCase,
    private val queueDetailsUseCase: QueueDetailsUseCase
) : BaseViewModel<QueueViewState, QueueEffect, QueueIntent, QueueAction, QueueNavigationEvent>() {
    private var offsetData = 0

    override fun initialState(): QueueViewState =
        QueueViewState.initialState(authUseCase.getCurrentUser())

    private fun startListening() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                queueUseCase.queueMessageFlow().collect {
                    when (it) {
                        is QueueMessage.CreateQueueMessage -> {
                            when (val result = queueDetailsUseCase.getQueueById(it.queueId)) {
                                is Result.Error -> {
                                    addIntermediateEffect(QueueEffect.AddQueueErrorEffect(R.string.message_queue_creation_error_uploading))
                                    delay(3000L)
                                    addIntermediateEffect(QueueEffect.DismissMessageEffect)
                                }
                                is Result.Success -> addIntermediateEffect(
                                    QueueEffect.AddQueueEffect(result.data)
                                )
                            }
                        }
                        is QueueMessage.DeleteQueueMessage -> addIntermediateEffect(
                            QueueEffect.DeleteQueueEffect(it.queueId)
                        )
                        is QueueMessage.RenameQueueMessage -> {
                            when (val result = queueDetailsUseCase.getQueueById(it.queueId)) {
                                is Result.Error -> {
                                    addIntermediateEffect(QueueEffect.RenameQueueErrorEffect(R.string.message_queue_renaming_error_uploading))
                                    delay(3000L)
                                    addIntermediateEffect(QueueEffect.DismissMessageEffect)
                                }
                                is Result.Success -> addIntermediateEffect(
                                    QueueEffect.RenameQueueEffect(result.data)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun intentInterpreter(intent: QueueIntent): QueueAction =
        when (intent) {
            QueueIntent.CreateQueueIntent -> QueueAction.CreateQueueAction
            QueueIntent.DeleteQueueIntent -> QueueAction.DeleteQueueAction
            QueueIntent.DismissCreateDialogIntent -> QueueAction.DismissDialogsAction
            QueueIntent.DismissDeleteDialogIntent -> QueueAction.DismissDialogsAction
            QueueIntent.DismissRenameDialogIntent -> QueueAction.DismissDialogsAction
            QueueIntent.InitialLoadingIntent -> QueueAction.InitialLoadingAction
            is QueueIntent.InputQueueNameIntent -> QueueAction.ChangeQueueNameAction(intent.queueName)
            QueueIntent.OpenCreateDialogIntent -> QueueAction.OpenCreateQueueDialogAction
            is QueueIntent.OpenDeleteDialogIntent -> QueueAction.OpenDeleteQueueDialogAction(intent.queueToDelete)
            is QueueIntent.OpenQueueDetailsIntent -> QueueAction.NavigateToQueueDetailsAction(intent.queue)
            is QueueIntent.OpenRenameDialogIntent -> QueueAction.OpenRenameQueueDialogAction(intent.queueToRename)
            QueueIntent.PagingLoadingIntent -> QueueAction.PagingLoadingAction
            QueueIntent.QueueNothingIntent -> throw Exception("nothing intent interpreting")
            QueueIntent.RenameQueueIntent -> QueueAction.RenameQueueAction
            QueueIntent.RetryInitialLoadingIntent -> QueueAction.InitialLoadingAction
            QueueIntent.RetryPagingLoadingIntent -> QueueAction.PagingLoadingAction
            QueueIntent.LogoutIntent -> QueueAction.LogoutAction
        }

    override suspend fun performAction(action: QueueAction): QueueEffect =
        when (action) {
            is QueueAction.ChangeQueueNameAction -> QueueEffect.NameChangedEffect(action.queueName)
            QueueAction.CreateQueueAction -> {
                if (viewStateLiveData.value?.queueName.isNullOrEmpty()) {
                    QueueEffect.NameErrorEffect(R.string.error_empty_field)
                } else {
                    addIntermediateEffect(QueueEffect.QueueActionPerformingEffect)
                    when (val result =
                        queueUseCase.createQueue(viewStateLiveData.value!!.queueName)) {
                        is Result.Error -> {
                            addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.error_queue_creation))
                            delay(3000L)
                            QueueEffect.DismissMessageEffect
                        }
                        is Result.Success -> {
                            addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.message_queue_creation_success))
                            delay(3000L)
                            QueueEffect.DismissMessageEffect
                        }
                    }
                }
            }
            QueueAction.DeleteQueueAction -> {
                addIntermediateEffect(QueueEffect.QueueActionPerformingEffect)
                when (val result =
                    queueUseCase.deleteQueue(viewStateLiveData.value!!.queueToDelete!!.id)) {
                    is Result.Error -> {
                        addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.error_queue_deletion))
                        delay(3000L)
                        QueueEffect.DismissMessageEffect
                    }
                    is Result.Success -> {
                        addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.message_queue_deletion_success))
                        delay(3000L)
                        QueueEffect.DismissMessageEffect
                    }
                }
            }
            QueueAction.DismissDialogsAction -> QueueEffect.DismissEffect
            QueueAction.InitialLoadingAction -> {
                addIntermediateEffect(QueueEffect.InitialLoadingEffect)
                when (val result = queueUseCase.getQueuePage(offsetData)) {
                    is Result.Error -> QueueEffect.InitialLoadingErrorEffect(R.string.error_queue_initial_loading)
                    is Result.Success -> QueueEffect.DataLoadedEffect(result.data.queues).also {
                        offsetData = result.data.nextOffset
                        startListening()
                    }
                }
            }
            is QueueAction.NavigateToQueueDetailsAction -> {
                navigationEventLiveData.postValue(
                    QueueNavigationEvent.NavigateToQueueDetailsEvent(
                        action.queue.id
                    )
                )
                QueueEffect.NoEffect
            }
            QueueAction.OpenCreateQueueDialogAction -> QueueEffect.CreateDialogEffect
            is QueueAction.OpenDeleteQueueDialogAction -> QueueEffect.DeleteDialogEffect(action.queueToDelete)
            is QueueAction.OpenRenameQueueDialogAction -> QueueEffect.RenameDialogEffect(action.queueToRename)
            QueueAction.PagingLoadingAction -> {
                if (offsetData == -1) {
                    QueueEffect.NoEffect
                } else {
                    addIntermediateEffect(QueueEffect.PagingLoadingEffect)
                    when (val result = queueUseCase.getQueuePage(offsetData)) {
                        is Result.Error -> QueueEffect.PagingLoadingErrorEffect(R.string.error_queue_paging_loading)
                        is Result.Success -> QueueEffect.DataLoadedEffect(result.data.queues).also {
                            offsetData = result.data.nextOffset
                        }
                    }
                }
            }
            QueueAction.RenameQueueAction -> {
                if (viewStateLiveData.value?.queueName.isNullOrEmpty()) {
                    QueueEffect.NameErrorEffect(R.string.error_empty_field)
                } else {
                    addIntermediateEffect(QueueEffect.QueueActionPerformingEffect)
                    when (val result = queueUseCase.renameQueue(
                        queue = viewStateLiveData.value!!.queueToRename!!,
                        name = viewStateLiveData.value!!.queueName
                    )) {
                        is Result.Error -> {
                            addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.error_queue_renaming))
                            delay(3000L)
                            QueueEffect.DismissMessageEffect
                        }
                        is Result.Success -> {
                            addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.message_queue_renaming_success))
                            delay(3000L)
                            QueueEffect.DismissMessageEffect
                        }
                    }
                }
            }
            QueueAction.LogoutAction -> {
                authUseCase.logout()
                navigationEventLiveData.postValue(QueueNavigationEvent.NavigateToLoginEvent)
                QueueEffect.NoEffect
            }
        }

    override fun stateReducer(
        oldState: QueueViewState,
        effect: QueueEffect
    ): QueueViewState =
        when (effect) {
            QueueEffect.CreateDialogEffect -> oldState.openCreateDialogState()
            is QueueEffect.DataLoadedEffect -> oldState.loadedState(
                data = effect.data
            )
            is QueueEffect.DeleteDialogEffect -> oldState.openDeleteDialogState(
                queueToDelete = effect.queueToDelete
            )
            QueueEffect.DismissEffect -> oldState.closeDialogState()
            QueueEffect.InitialLoadingEffect -> oldState.initialLoadingState()
            is QueueEffect.InitialLoadingErrorEffect -> oldState.initialLoadingErrorState(
                initialLoadingError = effect.message
            )
            is QueueEffect.NameChangedEffect -> oldState.inputQueueNameState(
                queueName = effect.queueName
            )
            QueueEffect.NoEffect -> oldState
            QueueEffect.PagingLoadingEffect -> oldState.pagingLoadingState()
            is QueueEffect.PagingLoadingErrorEffect -> oldState.pagingLoadingErrorState(
                pagingLoadingError = effect.message
            )
            is QueueEffect.RenameDialogEffect -> oldState.openRenameDialogState(
                queueToRename = effect.queueToRename
            )
            QueueEffect.QueueActionPerformingEffect -> oldState.performActionState()
            is QueueEffect.NameErrorEffect -> oldState.queueNameErrorState(
                queueNameError = effect.error
            )
            is QueueEffect.QueueActionMessageEffect -> oldState.messageState(
                messageText = effect.message
            )
            QueueEffect.DismissMessageEffect -> oldState.dismissMessageState()
            is QueueEffect.AddQueueEffect -> oldState.addQueueToTheTopState(
                queue = effect.queue
            )
            is QueueEffect.DeleteQueueEffect -> oldState.deleteQueueState(
                queueId = effect.queueId
            )
            is QueueEffect.AddQueueErrorEffect -> oldState.messageState(
                messageText = effect.message
            )
            is QueueEffect.RenameQueueEffect -> oldState.renameQueueState(
                queue = effect.queue
            )
            is QueueEffect.RenameQueueErrorEffect -> oldState.messageState(
                messageText = effect.message
            )
        }

}