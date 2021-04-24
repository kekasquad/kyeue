package io.kekasquad.queue.queues.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import io.kekasquad.queue.R
import io.kekasquad.queue.base.recycler.BaseEndlessAdapter
import io.kekasquad.queue.vo.inapp.Queue

class QueueAdapter(
    private val onClick: (Queue) -> Unit,
    onInitialErrorRetry: () -> Unit,
    onPagingErrorRetry: () -> Unit
) : BaseEndlessAdapter<Queue, QueueViewHolder>(onInitialErrorRetry, onPagingErrorRetry) {
    override fun getDataViewHolder(inflater: LayoutInflater, parent: ViewGroup): QueueViewHolder =
        QueueViewHolder(
            inflater.inflate(
                R.layout.item_queue,
                parent,
                false
            ),
            onClick
        )
}