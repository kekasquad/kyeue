package io.kekasquad.kyeue.ui.queue

import dagger.hilt.android.lifecycle.HiltViewModel
import io.kekasquad.kyeue.base.BaseViewModel
import io.kekasquad.kyeue.data.usecase.AuthUseCase
import io.kekasquad.kyeue.data.usecase.QueueUseCase
import io.kekasquad.kyeue.vo.inapp.Result
import javax.inject.Inject

@HiltViewModel
class QueueViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val queueUseCase: QueueUseCase
) : BaseViewModel<QueueViewState, QueueEffect, QueueIntent, QueueAction>() {
    private var offsetData = 0

    override fun initialState(): QueueViewState =
        QueueViewState.initialState(authUseCase.getCurrentUser())

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
                QueueEffect.NoEffect
            }
            QueueAction.DeleteQueueAction -> {
                QueueEffect.NoEffect
            }
            QueueAction.DismissDialogsAction -> QueueEffect.DismissEffect
            QueueAction.InitialLoadingAction -> {
                addIntermediateEffect(QueueEffect.InitialLoadingEffect)
                when (val result = queueUseCase.getQueuePage(offsetData)) {
                    is Result.Error -> QueueEffect.InitialLoadingErrorEffect(result.throwable)
                    is Result.Success -> QueueEffect.DataLoadedEffect(result.data.queues).also {
                        offsetData = result.data.nextOffset ?: -1
                    }
                }
            }
            is QueueAction.NavigateToQueueDetailsAction -> {
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
                            offsetData = result.data.nextOffset ?: -1
                        }
                    }
                }
            }
            QueueAction.RenameQueueAction -> {
                QueueEffect.NoEffect
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
                queueName = oldState.queueName
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
                queueToDelete = oldState.queueToDelete,
                queueName = effect.queueName
            )
            QueueEffect.NoEffect -> oldState
            QueueEffect.PagingLoadingEffect -> QueueViewState.pagingLoadingState(
                currentUser = oldState.currentUser,
                data = oldState.data
            )
            is QueueEffect.PagingLoadingErrorEffect -> QueueViewState.pagingLoadingErrorState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                pagingLoadingError = effect.throwable,
                isCreateDialogOpened = oldState.isCreateDialogOpened,
                queueToRename = oldState.queueToRename,
                queueToDelete = oldState.queueToDelete,
                queueName = oldState.queueName
            )
            is QueueEffect.RenameDialogEffect -> QueueViewState.openRenameDialogState(
                currentUser = oldState.currentUser,
                data = oldState.data,
                isPagingLoading = oldState.isPagingLoading,
                pagingLoadingError = oldState.pagingLoadingError,
                queueToRename = effect.queueToRename
            )
        }

}