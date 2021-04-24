package io.kekasquad.queue.queues.recycler

import android.view.View
import android.view.ViewGroup
import io.kekasquad.queue.base.recycler.DataViewHolder
import io.kekasquad.queue.vo.inapp.Queue
import kotlinx.android.synthetic.main.item_queue.view.*

class QueueViewHolder(
    private val root: View,
    onClick: (Queue) -> Unit
) : DataViewHolder<Queue>(root) {
    private lateinit var queue: Queue

    init {
        root.setOnClickListener { onClick(queue) }
    }

    override fun bindData(data: Queue) {
        this.queue = data
        root.item_queue_name.text = data.name
    }
}