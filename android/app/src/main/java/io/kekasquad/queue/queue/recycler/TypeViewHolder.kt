package io.kekasquad.queue.queue.recycler

import android.view.ViewGroup
import io.kekasquad.queue.base.recycler.DataViewHolder
import kotlinx.android.synthetic.main.item_type.view.*

class TypeViewHolder(
    private val root: ViewGroup
) : DataViewHolder<String>(root) {
    override fun bindData(data: String) {
        root.item_type_text.text = data
    }
}