package io.kekasquad.queue.queues

import io.kekasquad.queue.base.MviViewState
import io.kekasquad.queue.vo.inapp.Queue

data class QueuesViewState(
    val isInitialLoading: Boolean,
    val initialError: Throwable?,
    val data: List<Queue>,
    val offset: Int?,
    val searchString: String?,
    val isPagingLoading: Boolean,
    val pagingLoadingError: Throwable?
) : MviViewState {
    override fun log(): String = this.toString()

    companion object {
        val initialState = QueuesViewState(
            isInitialLoading = false,
            initialError = null,
            data = emptyList(),
            offset = 0,
            searchString = null,
            isPagingLoading = false,
            pagingLoadingError = null
        )

        val initialLoadingState = QueuesViewState(
            isInitialLoading = true,
            initialError = null,
            data = emptyList(),
            offset = 0,
            searchString = null,
            isPagingLoading = false,
            pagingLoadingError = null
        )

        fun initialErrorState(initialError: Throwable?) = QueuesViewState(
            isInitialLoading = false,
            initialError = initialError,
            data = emptyList(),
            offset = 0,
            searchString = null,
            isPagingLoading = false,
            pagingLoadingError = null
        )

        fun dataLoadedState(data: List<Queue>, nextOffset: Int?) =
            QueuesViewState(
                isInitialLoading = false,
                initialError = null,
                data = data,
                offset = nextOffset,
                searchString = null,
                isPagingLoading = false,
                pagingLoadingError = null
            )

        fun pagingLoadingState(data: List<Queue>, nextOffset: Int?) = QueuesViewState(
            isInitialLoading = false,
            initialError = null,
            data = data,
            offset = nextOffset,
            searchString = null,
            isPagingLoading = true,
            pagingLoadingError = null
        )

        fun pagingLoadingErrorState(
            data: List<Queue>,
            pagingLoadingError: Throwable?,
            nextOffset: Int?
        ) =
            QueuesViewState(
                isInitialLoading = false,
                initialError = null,
                data = data,
                offset = nextOffset,
                searchString = null,
                isPagingLoading = false,
                pagingLoadingError = pagingLoadingError
            )

    }

}