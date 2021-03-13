package io.kekasquad.queue.base.recycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ErrorViewHolder(
    view: View,
    onRetry: () -> Unit
) : RecyclerView.ViewHolder(view) {

    init {
//        view.retry_button.setOnClickListener { onRetry() }
    }

}