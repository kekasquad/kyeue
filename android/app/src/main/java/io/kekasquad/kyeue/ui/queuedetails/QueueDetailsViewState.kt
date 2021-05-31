package io.kekasquad.kyeue.ui.queuedetails

import androidx.annotation.StringRes
import io.kekasquad.kyeue.base.MviViewState
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.User

data class QueueDetailsViewState(
    val currentUser: User,
    val isLoading: Boolean,
    @StringRes val loadingErrorMessage: Int,
    val queue: Queue?,
    @StringRes val messageText: Int
) : MviViewState {

    fun loadingState() = QueueDetailsViewState(
        currentUser = this.currentUser,
        isLoading = true,
        loadingErrorMessage = 0,
        queue = null,
        messageText = 0
    )

    fun initialErrorState(loadingErrorMessage: Int) = QueueDetailsViewState(
        currentUser = this.currentUser,
        isLoading = false,
        loadingErrorMessage = loadingErrorMessage,
        queue = null,
        messageText = 0
    )

    fun loadedState(queue: Queue) = QueueDetailsViewState(
        currentUser = this.currentUser,
        isLoading = false,
        loadingErrorMessage = 0,
        queue = queue,
        messageText = this.messageText
    )

    fun messageState(messageText: Int) = QueueDetailsViewState(
        currentUser = this.currentUser,
        isLoading = false,
        loadingErrorMessage = 0,
        queue = this.queue,
        messageText = messageText
    )

    fun dismissMessageState() = QueueDetailsViewState(
        currentUser = this.currentUser,
        isLoading = false,
        loadingErrorMessage = 0,
        queue = this.queue,
        messageText = 0
    )

    companion object {
        fun initialState(currentUser: User) = QueueDetailsViewState(
            currentUser = currentUser,
            isLoading = true,
            loadingErrorMessage = 0,
            queue = null,
            messageText = 0
        )
    }

}