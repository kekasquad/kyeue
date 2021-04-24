package io.kekasquad.queue.queues

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.queue.R
import io.kekasquad.queue.base.BaseFragment
import io.kekasquad.queue.base.recycler.OffsetScrollListener
import io.kekasquad.queue.base.recycler.RecyclerEndlessState
import io.kekasquad.queue.data.usecase.QueueUseCaseImpl
import io.kekasquad.queue.queues.recycler.QueueAdapter
import kotlinx.android.synthetic.main.fragment_queues.*

@AndroidEntryPoint
class QueuesFragment : BaseFragment<QueuesViewState, QueuesIntent>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_queues
    override val viewModel: QueuesViewModel by viewModels()
    private lateinit var queueAdapter: QueueAdapter

    override fun backStackIntent(): QueuesIntent = QueuesIntent.QueuesNothingIntent

    override fun initialIntent(): QueuesIntent = QueuesIntent.InitialIntent

    override fun initViews() {
        fab_create_queue.setOnClickListener {
            _intentLiveData.value = QueuesIntent.QueuesNothingIntent
        }
        queueAdapter = QueueAdapter(
            { _intentLiveData.value = QueuesIntent.QueuesNothingIntent },
            { _intentLiveData.value = QueuesIntent.RetryInitialIntent },
            { _intentLiveData.value = QueuesIntent.PagingRetryLoadingIntent }
        )
        queue_recycler.adapter = queueAdapter
        queue_recycler.layoutManager = LinearLayoutManager(requireContext())
        queue_recycler.addOnScrollListener(
            OffsetScrollListener(
                mLayoutManager = queue_recycler.layoutManager as LinearLayoutManager,
                mOffset = 3,
                mPageSize = QueueUseCaseImpl.QUEUE_PAGE_SIZE_LIMIT,
                offsetListener = { _intentLiveData.value = QueuesIntent.PagingLoadingIntent }
            )
        )
    }

    override fun render(viewState: QueuesViewState) {
        val state = when {
            viewState.isInitialLoading -> RecyclerEndlessState.LOADING
            viewState.initialError != null -> RecyclerEndlessState.ERROR
            viewState.isPagingLoading -> RecyclerEndlessState.PAGING_LOADING
            viewState.pagingLoadingError != null -> RecyclerEndlessState.PAGING_ERROR
            viewState.data.isEmpty() -> RecyclerEndlessState.EMPTY
            else -> RecyclerEndlessState.ITEM
        }
        queueAdapter.updateData(viewState.data, state)
    }
}