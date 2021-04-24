package io.kekasquad.queue.base.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import io.kekasquad.queue.R

enum class RecyclerEndlessState(val viewType: Int) {
    LOADING(100000),
    ERROR(200000),
    EMPTY(300000),
    PAGING_LOADING(500000),
    PAGING_ERROR(600000),
    ITEM(1)
}

abstract class BaseEndlessAdapter<T, VH : DataViewHolder<T>>(
    private val onRetry: () -> Unit,
    private val onPagingRetry: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected val dataList: MutableList<T> = mutableListOf()
    private var state = RecyclerEndlessState.LOADING

    protected abstract fun getDataViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): VH

    protected open fun getLoadingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder = EmptyViewHolder(
        inflater.inflate(
            R.layout.item_loading,
            parent,
            false
        )
    )

    protected open fun getEmptyViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder = EmptyViewHolder(
        inflater.inflate(
            R.layout.item_empty,
            parent,
            false
        )
    )

    protected open fun getErrorViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder = ErrorViewHolder(
        inflater.inflate(
            R.layout.item_error,
            parent,
            false
        ),
        onRetry
    )

    protected open fun getPagingLoadingViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder = EmptyViewHolder(
        inflater.inflate(
            R.layout.item_paging_loading,
            parent,
            false
        )
    )

    protected open fun getPagingErrorViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ): RecyclerView.ViewHolder = ErrorViewHolder(
        inflater.inflate(
            R.layout.item_paging_error,
            parent,
            false
        ),
        onPagingRetry
    )

    fun updateData(newData: List<T>, state: RecyclerEndlessState) {
        val prevSize = itemCount
        dataList.clear()
        dataList.addAll(newData)
        this.state = state
        if (state == RecyclerEndlessState.ITEM) {
            notifyItemRemoved(prevSize - 1)
            if (itemCount >= prevSize) {
                notifyItemRangeInserted(prevSize - 1, itemCount - prevSize)
            }
        } else {
            if (itemCount == prevSize) {
                notifyItemChanged(prevSize - 1)
            } else {
                notifyItemInserted(prevSize)
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (position == this.itemCount - 1 && this.state != RecyclerEndlessState.ITEM) this.state.viewType
        else position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            RecyclerEndlessState.LOADING.viewType -> getLoadingViewHolder(layoutInflater, parent)
            RecyclerEndlessState.ERROR.viewType -> getErrorViewHolder(layoutInflater, parent)
            RecyclerEndlessState.EMPTY.viewType -> getEmptyViewHolder(layoutInflater, parent)
            RecyclerEndlessState.PAGING_LOADING.viewType -> getPagingLoadingViewHolder(
                layoutInflater,
                parent
            )
            RecyclerEndlessState.PAGING_ERROR.viewType -> getPagingErrorViewHolder(
                layoutInflater,
                parent
            )
            else -> getDataViewHolder(layoutInflater, parent)
        }
    }

    override fun getItemCount() = dataList.size + if (state != RecyclerEndlessState.ITEM) 1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) < RecyclerEndlessState.LOADING.viewType) {
            (holder as VH).bindData(dataList[position])
        } else {
            if (holder.itemView.layoutParams is StaggeredGridLayoutManager.LayoutParams) {
                (holder.itemView.layoutParams
                        as StaggeredGridLayoutManager.LayoutParams).isFullSpan = true
            }
        }
    }

}