package io.kekasquad.kyeue.ui.queue

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import io.kekasquad.kyeue.base.MviViewState
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.User

@Immutable
data class QueueViewState(
    val isInitialLoading: Boolean,
    val initialLoadingError: Throwable?,
    val data: List<Queue>,
    val currentUser: User,
    val isPagingLoading: Boolean,
    val pagingLoadingError: Throwable?,
    val isCreateDialogOpened: Boolean,
    val queueToRename: Queue?,
    val queueToDelete: Queue?,
    val queueName: String,
    val isActionPerforming: Boolean,
    @StringRes val queueNameError: Int,
    @StringRes val messageText: Int
) : MviViewState {
    companion object {
        fun initialState(currentUser: User) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = emptyList(),
            currentUser = currentUser,
            isPagingLoading = false,
            pagingLoadingError = null,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )

        fun initialLoadingState(currentUser: User) = QueueViewState(
            isInitialLoading = true,
            initialLoadingError = null,
            data = emptyList(),
            currentUser = currentUser,
            isPagingLoading = false,
            pagingLoadingError = null,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )

        fun initialLoadingErrorState(
            currentUser: User,
            initialLoadingError: Throwable
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = initialLoadingError,
            data = emptyList(),
            currentUser = currentUser,
            isPagingLoading = false,
            pagingLoadingError = null,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )

        fun loadedState(
            currentUser: User,
            data: List<Queue>,
            isCreateDialogOpened: Boolean,
            queueToRename: Queue?,
            queueToDelete: Queue?,
            queueName: String,
            isActionPerforming: Boolean,
            messageText: Int
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = false,
            pagingLoadingError = null,
            isCreateDialogOpened = isCreateDialogOpened,
            queueToRename = queueToRename,
            queueToDelete = queueToDelete,
            queueName = queueName,
            isActionPerforming = isActionPerforming,
            queueNameError = 0,
            messageText = messageText
        )

        fun pagingLoadingState(
            currentUser: User,
            data: List<Queue>,
            messageText: Int
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = true,
            pagingLoadingError = null,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = messageText
        )

        fun pagingLoadingErrorState(
            currentUser: User,
            data: List<Queue>,
            pagingLoadingError: Throwable,
            isCreateDialogOpened: Boolean,
            queueToDelete: Queue?,
            queueToRename: Queue?,
            queueName: String,
            isActionPerforming: Boolean,
            messageText: Int
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = false,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = isCreateDialogOpened,
            queueToRename = queueToRename,
            queueToDelete = queueToDelete,
            queueName = queueName,
            isActionPerforming = isActionPerforming,
            queueNameError = 0,
            messageText = messageText
        )

        fun openCreateDialogState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = true,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )

        fun openRenameDialogState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?,
            queueToRename: Queue
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = false,
            queueToRename = queueToRename,
            queueToDelete = null,
            queueName = queueToRename.name,
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )

        fun openDeleteDialogState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?,
            queueToDelete: Queue
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = queueToDelete,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )

        fun inputQueueNameState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?,
            queueToRename: Queue?,
            isCreateDialogOpened: Boolean,
            queueName: String,
            queueNameError: Int
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            queueToDelete = null,
            queueToRename = queueToRename,
            isCreateDialogOpened = isCreateDialogOpened,
            queueName = queueName,
            isActionPerforming = false,
            queueNameError = queueNameError,
            messageText = 0
        )

        fun closeDialogState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )

        fun performActionState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = true,
            queueNameError = 0,
            messageText = 0
        )

        fun queueNameErrorState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?,
            isCreateDialogOpened: Boolean,
            queueToRename: Queue?,
            queueName: String,
            queueNameError: Int
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = isCreateDialogOpened,
            queueToRename = queueToRename,
            queueToDelete = null,
            queueName = queueName,
            isActionPerforming = false,
            queueNameError = queueNameError,
            messageText = 0
        )

        fun messageState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?,
            messageText: Int
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = messageText
        )

        fun dismissMessageState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?,
            isCreateDialogOpened: Boolean,
            queueToRename: Queue?,
            queueToDelete: Queue?,
            queueName: String,
            isActionPerforming: Boolean,
            queueNameError: Int
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = isCreateDialogOpened,
            queueToRename = queueToRename,
            queueToDelete = queueToDelete,
            queueName = queueName,
            isActionPerforming = isActionPerforming,
            queueNameError = queueNameError,
            messageText = 0
        )

    }
}