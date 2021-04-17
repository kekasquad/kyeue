package io.kekasquad.queue.queues

import io.kekasquad.queue.base.MviViewState
import io.kekasquad.queue.vo.Queue

data class QueuesViewState(
    val isInitialLoading: Boolean,
    val initialError: Throwable?,
    val data: List<Queue>,
    val isPagingLoading: Boolean,
    val pagingLoadingError: Throwable?
) : MviViewState {
    override fun log(): String = this.toString()

    companion object {
        val initialState = QueuesViewState(
            isInitialLoading = false,
            initialError = null,
            data = emptyList(),
            isPagingLoading = false,
            pagingLoadingError = null
        )

        val initialLoadingState = QueuesViewState(
            isInitialLoading = true,
            initialError = null,
            data = emptyList(),
            isPagingLoading = false,
            pagingLoadingError = null
        )

        fun initialErrorState(initialError: Throwable?) = QueuesViewState(
            isInitialLoading = false,
            initialError = initialError,
            data = emptyList(),
            isPagingLoading = false,
            pagingLoadingError = null
        )

        fun dataLoadedState(data: List<Queue>) =
            QueuesViewState(
                isInitialLoading = false,
                initialError = null,
                data = data,
                isPagingLoading = false,
                pagingLoadingError = null
            )

        fun pagingLoadingState(data: List<Queue>) = QueuesViewState(
            isInitialLoading = false,
            initialError = null,
            data = data,
            isPagingLoading = true,
            pagingLoadingError = null
        )

        fun pagingLoadingErrorState(data: List<Queue>, pagingLoadingError: Throwable?) =
            QueuesViewState(
                isInitialLoading = false,
                initialError = null,
                data = data,
                isPagingLoading = false,
                pagingLoadingError = pagingLoadingError
            )

    }

}