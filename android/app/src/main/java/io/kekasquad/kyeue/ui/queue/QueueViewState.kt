package io.kekasquad.kyeue.ui.queue

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import io.kekasquad.kyeue.base.MviViewState
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.User

@Immutable
data class QueueViewState(
    val isInitialLoading: Boolean,
    @StringRes val initialLoadingError: Int,
    val data: List<Queue>,
    val currentUser: User,
    val isPagingLoading: Boolean,
    @StringRes val pagingLoadingError: Int,
    val isCreateDialogOpened: Boolean,
    val queueToRename: Queue?,
    val queueToDelete: Queue?,
    val queueName: String,
    val isActionPerforming: Boolean,
    @StringRes val queueNameError: Int,
    @StringRes val messageText: Int
) : MviViewState {

    fun initialLoadingState() = QueueViewState(
        isInitialLoading = true,
        initialLoadingError = 0,
        data = emptyList(),
        currentUser = currentUser,
        isPagingLoading = false,
        pagingLoadingError = 0,
        isCreateDialogOpened = false,
        queueToRename = null,
        queueToDelete = null,
        queueName = "",
        isActionPerforming = false,
        queueNameError = 0,
        messageText = 0
    )

    fun initialLoadingErrorState(
        initialLoadingError: Int
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = initialLoadingError,
        data = emptyList(),
        currentUser = this.currentUser,
        isPagingLoading = false,
        pagingLoadingError = 0,
        isCreateDialogOpened = false,
        queueToRename = null,
        queueToDelete = null,
        queueName = "",
        isActionPerforming = false,
        queueNameError = 0,
        messageText = 0
    )

    fun loadedState(
        data: List<Queue>
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data.toMutableList().apply {
            var valuesToDrop = 0
            val newDataSet = data.toHashSet()
            for (i in this.size - 1 downTo 0) {
                if (!newDataSet.contains(this.get(i))) {
                    break
                }
                valuesToDrop++
            }
            this.addAll(data.drop(valuesToDrop))
        },
        currentUser = this.currentUser,
        isPagingLoading = false,
        pagingLoadingError = 0,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueToRename = this.queueToRename,
        queueToDelete = this.queueToDelete,
        queueName = this.queueName,
        isActionPerforming = this.isActionPerforming,
        queueNameError = this.queueNameError,
        messageText = this.messageText
    )

    fun pagingLoadingState() = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = true,
        pagingLoadingError = 0,
        isCreateDialogOpened = false,
        queueToRename = null,
        queueToDelete = null,
        queueName = "",
        isActionPerforming = false,
        queueNameError = 0,
        messageText = this.messageText
    )

    fun pagingLoadingErrorState(
        pagingLoadingError: Int
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = false,
        pagingLoadingError = pagingLoadingError,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueToRename = this.queueToRename,
        queueToDelete = this.queueToDelete,
        queueName = this.queueName,
        isActionPerforming = this.isActionPerforming,
        queueNameError = 0,
        messageText = this.messageText
    )

    fun openCreateDialogState() = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = true,
        queueToRename = null,
        queueToDelete = null,
        queueName = "",
        isActionPerforming = false,
        queueNameError = 0,
        messageText = 0
    )

    fun openRenameDialogState(
        queueToRename: Queue
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = false,
        queueToRename = queueToRename,
        queueToDelete = null,
        queueName = queueToRename.name,
        isActionPerforming = false,
        queueNameError = 0,
        messageText = 0
    )

    fun openDeleteDialogState(
        queueToDelete: Queue
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = false,
        queueToRename = null,
        queueToDelete = queueToDelete,
        queueName = "",
        isActionPerforming = false,
        queueNameError = 0,
        messageText = 0
    )

    fun inputQueueNameState(
        queueName: String
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        queueToDelete = null,
        queueToRename = this.queueToRename,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueName = queueName,
        isActionPerforming = false,
        queueNameError = this.queueNameError,
        messageText = 0
    )

    fun closeDialogState() = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
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

    fun performActionState() = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = false,
        queueToRename = null,
        queueToDelete = null,
        queueName = "",
        isActionPerforming = true,
        queueNameError = 0,
        messageText = 0
    )

    fun queueNameErrorState(
        queueNameError: Int
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueToRename = this.queueToRename,
        queueToDelete = null,
        queueName = this.queueName,
        isActionPerforming = false,
        queueNameError = queueNameError,
        messageText = 0
    )

    fun messageState(
        messageText: Int
    ) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = false,
        queueToRename = null,
        queueToDelete = null,
        queueName = "",
        isActionPerforming = false,
        queueNameError = 0,
        messageText = messageText
    )

    fun dismissMessageState() = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data,
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueToRename = this.queueToRename,
        queueToDelete = this.queueToDelete,
        queueName = this.queueName,
        isActionPerforming = this.isActionPerforming,
        queueNameError = this.queueNameError,
        messageText = 0
    )

    fun addQueueToTheTopState(queue: Queue) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data.toMutableList().apply {
            add(0, queue)
        },
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueToRename = this.queueToRename,
        queueToDelete = this.queueToDelete,
        queueName = this.queueName,
        isActionPerforming = this.isActionPerforming,
        queueNameError = this.queueNameError,
        messageText = this.messageText
    )

    fun deleteQueueState(queueId: String) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data.toMutableList().apply {
            for (i in indices) {
                if (this[i].id == queueId) {
                    this.removeAt(i)
                    break
                }
            }
        },
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueToRename = this.queueToRename,
        queueToDelete = this.queueToDelete,
        queueName = this.queueName,
        isActionPerforming = this.isActionPerforming,
        queueNameError = this.queueNameError,
        messageText = this.messageText
    )

    fun renameQueueState(queue: Queue) = QueueViewState(
        isInitialLoading = false,
        initialLoadingError = 0,
        data = this.data.toMutableList().apply {
            for (i in indices) {
                if (this[i].id == queue.id) {
                    this.removeAt(i)
                    this.add(i, queue)
                    break
                }
            }
        },
        currentUser = this.currentUser,
        isPagingLoading = this.isPagingLoading,
        pagingLoadingError = this.pagingLoadingError,
        isCreateDialogOpened = this.isCreateDialogOpened,
        queueToRename = this.queueToRename,
        queueToDelete = this.queueToDelete,
        queueName = this.queueName,
        isActionPerforming = this.isActionPerforming,
        queueNameError = this.queueNameError,
        messageText = this.messageText
    )

    companion object {
        fun initialState(currentUser: User) = QueueViewState(
            isInitialLoading = true,
            initialLoadingError = 0,
            data = emptyList(),
            currentUser = currentUser,
            isPagingLoading = false,
            pagingLoadingError = 0,
            isCreateDialogOpened = false,
            queueToRename = null,
            queueToDelete = null,
            queueName = "",
            isActionPerforming = false,
            queueNameError = 0,
            messageText = 0
        )
    }
}