package io.kekasquad.queue.queue

import io.kekasquad.queue.base.MviViewState
import io.kekasquad.queue.vo.User

data class QueueViewState(
    val isInitialLoading: Boolean,
    val initialError: Throwable?,
    val data: List<User>,
    val isPagingLoading: Boolean,
    val pagingLoadingError: Throwable?
) : MviViewState {
    override fun log(): String = this.toString()

    companion object {
        val initialState = QueueViewState(
            isInitialLoading = false,
            initialError = null,
            data = emptyList(),
            isPagingLoading = false,
            pagingLoadingError = null
        )

        val initialLoadingState = QueueViewState(
            isInitialLoading = true,
            initialError = null,
            data = emptyList(),
            isPagingLoading = false,
            pagingLoadingError = null
        )

        fun initialErrorState(initialError: Throwable?) = QueueViewState(
            isInitialLoading = false,
            initialError = initialError,
            data = emptyList(),
            isPagingLoading = false,
            pagingLoadingError = null
        )

        fun dataLoadedState(data: List<User>) =
            QueueViewState(
                isInitialLoading = false,
                initialError = null,
                data = data,
                isPagingLoading = false,
                pagingLoadingError = null
            )

        fun pagingLoadingState(data: List<User>) = QueueViewState(
            isInitialLoading = false,
            initialError = null,
            data = data,
            isPagingLoading = true,
            pagingLoadingError = null
        )

        fun pagingLoadingErrorState(data: List<User>, pagingLoadingError: Throwable?) =
            QueueViewState(
                isInitialLoading = false,
                initialError = null,
                data = data,
                isPagingLoading = false,
                pagingLoadingError = pagingLoadingError
            )

    }

}