package io.kekasquad.queue.queue

import io.kekasquad.queue.base.MviViewState

data class QueueViewState(
    val isInitialLoading: Boolean,
    val initialError: Throwable?,
    val data: List<String>
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

        fun dataLoadedState(data: List<String>) = QueueViewState(
            isInitialLoading = false,
            initialError = null,
            data = data
        )
    }

}