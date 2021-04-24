package io.kekasquad.queue.queue

import io.kekasquad.queue.base.MviViewState
import io.kekasquad.queue.vo.inapp.User
import io.kekasquad.queue.vo.remote.UserRemote

data class QueueViewState(
    val isInitialLoading: Boolean,
    val initialError: Throwable?,
    val data: List<User>
) : MviViewState {
    override fun log(): String = this.toString()

    companion object {
        val initialState = QueueViewState(
            isInitialLoading = false,
            initialError = null,
            data = emptyList()
        )

        val initialLoadingState = QueueViewState(
            isInitialLoading = true,
            initialError = null,
            data = emptyList()
        )

        fun initialErrorState(initialError: Throwable?) = QueueViewState(
            isInitialLoading = false,
            initialError = initialError,
            data = emptyList()
        )

        fun dataLoadedState(data: List<User>) =
            QueueViewState(
                isInitialLoading = false,
                initialError = null,
                data = data
            )

    }

}