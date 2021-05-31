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
                            addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.error_queue_loading_error_message))
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
                        addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.error_queue_loading_error_message))
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
                    is Result.Error -> QueueEffect.InitialLoadingErrorEffect(result.throwable)
                    is Result.Success -> QueueEffect.DataLoadedEffect(result.data.queues).also {
                        offsetData = result.data.nextOffset
                        startListening()
                    }
                }
            }
            is QueueAction.NavigateToQueueDetailsAction -> {
                navigationEventLiveData.postValue(QueueNavigationEvent.NavigateToQueueDetails(action.queue.id))
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
                        is Result.Error -> QueueEffect.PagingLoadingErrorEffect(result.throwable)
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
                            addIntermediateEffect(QueueEffect.QueueActionMessageEffect(R.string.error_queue_loading_error_message))
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
        }

    override fun stateReducer(
        oldState: QueueViewState,
        effect: QueueEffect
    ): QueueViewState =
        when (effect) {
            QueueEffect.CreateDialogEffect -> QueueViewState.openCreateDialogState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError
            )
            is QueueEffect.DataLoadedEffect -> QueueViewState.loadedState(
                currentUser = oldState.currentUser,
                data = oldState.data + effect.data,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                queueNameError = oldState.queueNameError,
                messageText = oldState.messageText,
                isActionPerforming = oldState.isActionPerforming
            )
            is QueueEffect.DeleteDialogEffect -> QueueViewState.openDeleteDialogState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                queueToDelete = effect.queueToDelete
            )
            QueueEffect.DismissEffect -> QueueViewState.closeDialogState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError
            )
            QueueEffect.InitialLoadingEffect -> QueueViewState.initialLoadingState(
                currentUser = oldState.currentUser
            )
            is QueueEffect.InitialLoadingErrorEffect -> QueueViewState.initialLoadingErrorState(
                currentUser = oldState.currentUser,
                initialLoadingError = effect.throwable
            )
            is QueueEffect.NameChangedEffect -> QueueViewState.inputQueueNameState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                queueToRename = oldState.queueToRename,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueName = effect.queueName,
                queueNameError = oldState.queueNameError
            )
            QueueEffect.NoEffect -> oldState
            QueueEffect.PagingLoadingEffect -> QueueViewState.pagingLoadingState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                messageText = oldState.messageText
            )
            is QueueEffect.PagingLoadingErrorEffect -> QueueViewState.pagingLoadingErrorState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                pagingLoadingError = effect.throwable,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                messageText = oldState.messageText,
                isActionPerforming = oldState.isActionPerforming
            )
            is QueueEffect.RenameDialogEffect -> QueueViewState.openRenameDialogState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                queueToRename = effect.queueToRename
            )
            QueueEffect.QueueActionPerformingEffect -> QueueViewState.performActionState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError
            )
            is QueueEffect.NameErrorEffect -> QueueViewState.queueNameErrorState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueName = oldState.queueName,
                queueNameError = effect.error
            )
            is QueueEffect.QueueActionMessageEffect -> QueueViewState.messageState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                messageText = effect.message
            )
            QueueEffect.DismissMessageEffect -> QueueViewState.dismissMessageState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                queueNameError = oldState.queueNameError,
                isActionPerforming = oldState.isActionPerforming
            )
            is QueueEffect.AddQueueEffect -> QueueViewState.messageUpdateState(
                currentUser = oldState.currentUser,
                data = oldState.data.toMutableList().apply {
                    add(0, effect.queue)
                },
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                queueNameError = oldState.queueNameError,
                messageText = oldState.messageText,
                isActionPerforming = oldState.isActionPerforming
            )
            is QueueEffect.DeleteQueueEffect -> QueueViewState.messageUpdateState(
                currentUser = oldState.currentUser,
                data = oldState.data.toMutableList().apply {
                    for (i in indices) {
                        if (this[i].id == effect.queueId) {
                            this.removeAt(i)
                            break
                        }
                    }
                },
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                queueNameError = oldState.queueNameError,
                messageText = oldState.messageText,
                isActionPerforming = oldState.isActionPerforming
            )
            is QueueEffect.AddQueueErrorEffect -> QueueViewState.messageUpdateState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                queueNameError = oldState.queueNameError,
                messageText = effect.message,
                isActionPerforming = oldState.isActionPerforming
            )
            is QueueEffect.RenameQueueEffect -> QueueViewState.messageUpdateState(
                currentUser = oldState.currentUser,
                data = oldState.data.toMutableList().apply {
                    for (i in indices) {
                        if (this[i].id == effect.queue.id) {
                            this.removeAt(i)
                            this.add(i, effect.queue)
                            break
                        }
                    }
                },
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                queueNameError = oldState.queueNameError,
                messageText = oldState.messageText,
                isActionPerforming = oldState.isActionPerforming
            )
            is QueueEffect.RenameQueueErrorEffect -> QueueViewState.messageUpdateState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName,
                queueNameError = oldState.queueNameError,
                messageText = effect.message,
                isActionPerforming = oldState.isActionPerforming
            )
        }

}