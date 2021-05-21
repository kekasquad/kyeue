package io.kekasquad.kyeue.ui.queue

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
    val queueName: String
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
            queueName = ""
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
            queueName = ""
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
            queueName = ""
        )

        fun loadedState(
            currentUser: User,
            data: List<Queue>,
            isCreateDialogOpened: Boolean,
            queueToRename: Queue?,
            queueToDelete: Queue?,
            queueName: String
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
            queueName = queueName
        )

        fun pagingLoadingState(
            currentUser: User,
            data: List<Queue>
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
            queueName = ""
        )

        fun pagingLoadingErrorState(
            currentUser: User,
            data: List<Queue>,
            pagingLoadingError: Throwable,
            isCreateDialogOpened: Boolean,
            queueToDelete: Queue?,
            queueToRename: Queue?,
            queueName: String
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
            queueName = queueName
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
            queueName = ""
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
            queueName = queueToRename.name
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
            queueName = ""
        )

        fun inputQueueNameState(
            currentUser: User,
            data: List<Queue>,
            isPagingLoading: Boolean,
            pagingLoadingError: Throwable?,
            queueToRename: Queue?,
            queueToDelete: Queue?,
            queueName: String
        ) = QueueViewState(
            isInitialLoading = false,
            initialLoadingError = null,
            data = data,
            currentUser = currentUser,
            isPagingLoading = isPagingLoading,
            pagingLoadingError = pagingLoadingError,
            isCreateDialogOpened = false,
            queueToRename = queueToRename,
            queueToDelete = queueToDelete,
            queueName = queueName
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
            queueName = ""
        )

    }
}